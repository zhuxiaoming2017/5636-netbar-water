package com.water.common.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.water.modules.model.AskQuestion;
import com.water.modules.model.BbsTopic;
public class LuceneUtil {
	private static Logger log = LoggerFactory.getLogger(LuceneUtil.class);

	// 版本
	private static final Version LUCENE_VERSION = Version.LUCENE_44;
	// 索引配置
	private static IndexWriterConfig config = null;
	// 分词器
	private static Analyzer analyzer = new IKAnalyzer();
	// 查询域
	private static final String[] zx_fields = new String[] { "title", "description" };
	// 查询域
	private static final String[] ask_fields = new String[] { "qtitle", "qcontent" };
	// 查询域
	private static final String[] bbs_fields = new String[] { "title"};
	// 查询字段权重
	// 同时搜索name和descr两个field，并设定它们在搜索结果排序过程中的权重，权重越高，排名越靠前
	private static Map<String, Float> zx_boosts = new HashMap<String, Float>();
	private static Map<String, Float> ask_boosts = new HashMap<String, Float>();
	private static Map<String, Float> bbs_boosts = new HashMap<String, Float>();

	static {
		config = new IndexWriterConfig(LUCENE_VERSION, analyzer);
		// 最大缓存文档数,控制写入一个新的segment前内存中保存的document的数目
		config.setMaxBufferedDocs(100);
		// 控制一个segment中可以保存的最大document数目，值较大有利于追加索引的速度，默认Integer.MAX_VALUE，无需修改。
		config.setMaxBufferedDocs(Integer.MAX_VALUE);
		// 设置权重
		zx_boosts.put("title", 1.0f);
		zx_boosts.put("description", 1.0f);

		ask_boosts.put("qtitle", 1.0f);
		ask_boosts.put("qcontent", 1.0f);

		bbs_boosts.put("title", 1.0f);
		bbs_boosts.put("content", 1.0f);
	}

	/**
	 * 搜索问答
	 * @param queryParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static PageInfo<AskQuestion> searchAskQuestionIndex(String queryParam,String searchSort ,int pageNo, int pageSize, String indexPath) {
		if (StringUtils.isBlank(queryParam)) {
			return null;
		}
		PageInfo<AskQuestion> pageInfo = new PageInfo<AskQuestion>();
		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		List<AskQuestion> artList = new ArrayList<AskQuestion>();
		Directory directory = null;
		DirectoryReader dReader = null;
		TokenStream tokenStream = null;
		try {
			// 设置要查询的索引目录
			directory = FSDirectory.open(new File(indexPath));
			dReader = DirectoryReader.open(directory);
			// 创建indexSearcher
			IndexSearcher searcher = new IndexSearcher(dReader);
			// 用MultiFieldQueryParser类实现对同一关键词的跨域搜索
			MultiFieldQueryParser parser = new MultiFieldQueryParser(LUCENE_VERSION, ask_fields, analyzer, ask_boosts);
			// 查询字符串
			Query query = parser.parse(queryParam);
			QueryScorer scorer = new QueryScorer(query);
			log.info("query:" + query.toString());
			// 查询
			TopDocs topDocs = null;
			if("keys".equals(searchSort)){
				topDocs= searcher.search(query, null, pageNo * pageSize);
			}else if("times".equals(searchSort)){
				topDocs = searcher.search(query, null, pageNo * pageSize, new Sort(new SortField("qtime",
					SortField.Type.INT, true)));
			}
			// 总页数
			pageInfo.setTotalCount(topDocs.totalHits);
			if (topDocs != null) {
				// 设置高亮
				SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
				Highlighter highlighter = new Highlighter(fors, scorer);
				Document doc = null;
				AskQuestion ask = null;
				// 遍历封装查询结果
				for (int i = (pageNo - 1) * pageSize; i < topDocs.scoreDocs.length; i++) {
					int docId = topDocs.scoreDocs[i].doc;
					doc = searcher.doc(docId);
					// 高亮qtitle域
					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), docId, "qtitle", analyzer);
					highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
					String qtitle = highlighter.getBestFragment(tokenStream, doc.get("qtitle"));
					// 高亮qcontent域
					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), docId, "qcontent", analyzer);
					highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
					String qcontent = highlighter.getBestFragment(tokenStream, doc.get("qcontent"));
					ask = new AskQuestion();
					ask.setId(Integer.valueOf(doc.get("qid")));// 问题id
					ask.setQtitle(StringUtils.isNotBlank(qtitle) ? qtitle : doc.get("qtitle"));// 问题标题
					ask.setQcontent(StringUtils.isNotBlank(qcontent) ? qcontent : doc.get("qcontent"));// 问题内容
					String qtime = doc.get("qtime");
					ask.setQtime(Integer.valueOf(qtime));// 提问时间
					ask.setQtimeStr(DateUtils.betweenTime(new Date(Long.valueOf(qtime)), "yyyy-MM-dd"));
					ask.setIsnew(betweenTime(new Date(Long.valueOf(qtime)), 12));
					ask.setCid(Integer.valueOf(doc.get("cid")));// 问题类型id
					ask.setCname(doc.get("cname"));// 问题类型名
					ask.setUid(Integer.valueOf(doc.get("uid")));// 提问用户id
					ask.setUname(StringUtils.isNotBlank(doc.get("uname")) ? doc.get("uname") : "");// 提问用户名
					ask.setIsresolve(Integer.valueOf(doc.get("isresolve")));// 是否解决
					artList.add(ask);
				}
			}
			// 查询记录
			pageInfo.setData(artList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dReader != null) {
				try {
					dReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pageInfo;
	}

	/**
	 * 搜索论坛
	 * @param queryParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static PageInfo<BbsTopic> searchBbsTopicIndex(String queryParam , String searchSort , int pageNo, int pageSize, String indexPath) {
		if (StringUtils.isBlank(queryParam)) {
			return null;
		}
		PageInfo<BbsTopic> pageInfo = new PageInfo<BbsTopic>();
		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		List<BbsTopic> bbsList = new ArrayList<BbsTopic>();
		Directory directory = null;
		DirectoryReader dReader = null;
		TokenStream tokenStream = null;
		try {
			// 设置要查询的索引目录
			directory = FSDirectory.open(new File(indexPath));
			dReader = DirectoryReader.open(directory);
			// 创建indexSearcher
			IndexSearcher searcher = new IndexSearcher(dReader);
			// 用MultiFieldQueryParser类实现对同一关键词的跨域搜索
			MultiFieldQueryParser parser = new MultiFieldQueryParser(LUCENE_VERSION, bbs_fields, analyzer, bbs_boosts);


			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String lowerDate = sdf.format(DateUtils.getAddYear(new Date(),-1));
			//只查询一年内的帖子
			TermRangeQuery termRangeQuery =TermRangeQuery.newStringRange("createTime_str",lowerDate,sdf.format(new Date()),true,true);

			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(termRangeQuery, BooleanClause.Occur.MUST);

			Query query = parser.parse(queryParam);
			booleanQuery.add(query, BooleanClause.Occur.MUST);

			QueryScorer scorer = new QueryScorer(booleanQuery);
			log.info("query:" + query.toString());
            // 查询
            TopDocs topDocs = null;
            if("keys".equals(searchSort)){
                topDocs= searcher.search(booleanQuery, null, pageNo * pageSize);
            }else if("times".equals(searchSort)){
                topDocs = searcher.search(booleanQuery, null, pageNo * pageSize, new Sort(new SortField("createTime_str",
                        SortField.Type.LONG, true)));
            }

			// 总页数
			pageInfo.setTotalCount(topDocs.totalHits);
			if (topDocs != null) {
				// 设置高亮
				SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
				Highlighter highlighter = new Highlighter(fors, scorer);
				Document doc = null;
				BbsTopic bbsTopic = null;
				// 遍历封装查询结果
				for (int i = (pageNo - 1) * pageSize; i < topDocs.scoreDocs.length; i++) {
					int docId = topDocs.scoreDocs[i].doc;
					doc = searcher.doc(docId);
					// 高亮qtitle域
					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), docId, "title", analyzer);
					highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
					String title = highlighter.getBestFragment(tokenStream, doc.get("title"));
					// 高亮qcontent域
					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), docId, "content", analyzer);
					highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
					String content = highlighter.getBestFragment(tokenStream, doc.get("content"));
					bbsTopic = new BbsTopic();
					bbsTopic.setTopicId(Integer.valueOf(doc.get("topicId")));// 主题id
					bbsTopic.setTitle(StringUtils.isNotBlank(title) ? title : doc.get("title"));// 帖子主题
					bbsTopic.setContent(StringUtils.isNotBlank(content) ? content : doc.get("content"));// 帖子内容
					bbsTopic.setAuthorId(Integer.valueOf(doc.get("authorId")));// 帖子作者id
					bbsTopic.setAuthor(StringUtils.isNotBlank(doc.get("author")) ? doc.get("author") : "");// 帖子作者
					String createTime = doc.get("createTime");
					try {
						SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						bbsTopic.setCreateTime(sdf1.parse(createTime));// 帖子时间
					} catch (Exception e) {
						bbsTopic.setCreateTime(new Date(createTime));
					}
					bbsTopic.setCreateTimeStr(DateUtils.betweenTime(bbsTopic.getCreateTime(), "yyyy-MM-dd"));// 帖子时间
					bbsTopic.setForumId(Integer.valueOf(doc.get("forumId")));// 帖子板块Id
					bbsTopic.setForumName(doc.get("forumName"));// 帖子板块
					bbsTopic.setViewCount(Integer.valueOf(doc.get("viewCount")));// 浏览次数
					bbsTopic.setReplyCount(Integer.valueOf(doc.get("replyCount")));// 回复次数
					bbsTopic.setIshot(StringUtils.isNotBlank(doc.get("ishot")) ? Integer.parseInt(doc.get("ishot")) : 0); //是否热帖：0-否，1-热帖
					bbsTopic.setIstop(StringUtils.isNotBlank(doc.get("istop")) ? Integer.parseInt(doc.get("istop")) : 0);//置顶：0-否，1-置顶
					bbsTopic.setIsrecommend(StringUtils.isNotBlank(doc.get("isrecommend")) ? Integer.parseInt(doc.get("isrecommend")) : 0);//是否推荐:0-否，1-是
					bbsTopic.setDigest(StringUtils.isNotBlank(doc.get("digest")) ? Integer.parseInt(doc.get("digest")) : 0);//是否精华,1精华,0非精华
					bbsList.add(bbsTopic);
				}
			}
			// 查询记录
			pageInfo.setData(bbsList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dReader != null) {
				try {
					dReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pageInfo;
	}


	/**
	 * 时间间隔
	 * @param stime
	 * @param num
	 * @return
	 */
	public static int betweenTime(Date stime, int num){
		if (stime != null) {
			Date curr = new Date();
			Calendar beginDate = DateUtils.getCalendar(stime);
			Calendar endDate = DateUtils.getCalendar(curr);
			int day = DateUtils.betweenDays(beginDate, endDate);
			if(day <=0){
				long time = curr.getTime() - stime.getTime();
				return time - num*60*60*1000 > 0 ? 0 : 1;
			}
		}
		return 0;
	}

	/**
	 * 获取img标签中的src值
	 * @param content
	 * @return
	 */
	public static List<String> getImgSrc(String content){
		List<String> list = new ArrayList<String>();
		//目前img标签标示有3种表达式
		//<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
		//开始匹配content中的<img />标签
		Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
		Matcher m_img = p_img.matcher(content);
		boolean result_img = m_img.find();
		if (result_img) {
			while (result_img) {
				//获取到匹配的<img />标签中的内容
				String str_img = m_img.group(2);
				//开始匹配<img />标签中的src
				Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
				Matcher m_src = p_src.matcher(str_img);
				if (m_src.find()) {
					String str_src = m_src.group(3);
					list.add(str_src);
				}
				//结束匹配<img />标签中的src
				//匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
				result_img = m_img.find();
			}
		}
		return list;
	}


	public static void main(String[] args) throws Exception {
		// addSingle();
		// add();
//		query();
		String content = "<p>\n" +
				"\t　　<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" />" +
				"<img alt=\"\" src=\"/netbar/uploads/120216/6-1202161626011F.jpg\" style=\"width: 594px; height: 306px\" /></p>\n" +
				"<p>\n" ;
		List<String> list = getImgSrc(content);
		System.out.println("size：" + list.size());
		System.out.println(list);
		String a="Wed Jul 25 16:12:17 CST 2012";
		Date d = new Date(a);
		String f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
		System.out.println(f);
	}

}
