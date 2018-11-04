package com.water.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.water.common.utils.Base64DecodeUtil;
import com.water.common.utils.Constants;
import com.water.common.utils.LuceneUtil;
import com.water.common.utils.PageInfo;
import com.water.component.bean.Properties;
import com.water.modules.model.BbsTopic;


/**
 * 搜索
 * Created by jiangdan on 2017/10/24.
 */
@Controller
@RequestMapping("/")
public class SearchController {

    private static Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Properties properties;

    /**
     * 处理论坛数据
     * @param bbspage
     */
    private void getBbsTopicData(PageInfo<BbsTopic> bbspage, String defaultUserImgUrl){
        if(bbspage != null && bbspage.getData()!=null && bbspage.getData().size() > 0){
            for(Object obj : bbspage.getData()){
                BbsTopic bbsTopic = (BbsTopic) obj;
                String userJson = stringRedisTemplate.opsForValue().get(Constants.USER_KEY + Base64DecodeUtil.base64Encode(bbsTopic.getAuthor()));
                bbsTopic.setAuthorPic(defaultUserImgUrl);
                if(StringUtils.isNotBlank(userJson)){
                    Map<String, Object> userMap = JSON.parseObject(userJson, Map.class);
                    if (userMap != null && userMap.containsKey("picture") && userMap.get("picture") != null
                            && StringUtils.isNotBlank(userMap.get("picture").toString())) {
                        String bbsImgUrl = properties.getBbsUserImgDownloadUrl();
                        bbsTopic.setAuthorPic(bbsImgUrl + userMap.get("picture"));
                    }
                }
            }
        }
    }

    /**
     * 搜索论坛
     */
    @RequestMapping("/search-bbs-data")
    @ResponseBody
    public Map<String, Object> bbsData(HttpServletRequest request) {
        String keyword = request.getParameter("search_keywords");
        String sort = request.getParameter("sort");
        if(StringUtils.isEmpty(sort)||"undefined".equals(sort)){
            sort ="keys";
        }
        log.info(sort);
        Map<String, Object> resultMap = new HashMap<>();
        if(StringUtils.isBlank(keyword)){
            return resultMap;
        }
        int pageNo = Integer.parseInt(request.getParameter("num") != null ? request.getParameter("num") : "1");
        int pageSize = Integer.parseInt(request.getParameter("size") != null ? request.getParameter("size") : "15");
        //默认头像
        String defaultUserImgUrl = properties.getDefaultUserImgUrl();
        //资讯
        PageInfo<BbsTopic> bbspage = LuceneUtil.searchBbsTopicIndex(keyword,sort, pageNo, pageSize, properties.getBbsLuceneUrl());
        getBbsTopicData(bbspage, defaultUserImgUrl);
        resultMap.put("list", bbspage.getData());
        return resultMap;
    }
}