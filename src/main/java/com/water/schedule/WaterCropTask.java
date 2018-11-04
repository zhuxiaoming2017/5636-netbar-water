package com.water.schedule;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.water.modules.model.ZxSysConfig;
import com.water.service.SysConfigService;

/**
 * 同步结算数据
 */
//@Component
//@EnableScheduling
public class WaterCropTask {
    private static Logger logger = LoggerFactory.getLogger(WaterCropTask.class);

    
    
    //图片的后缀(目前只截取jpg和png,gif的没有添加水印)
  	public static final String[] IMAGE_EXT = new String[] { "jpg", "png"};
  	
  	/**
     * jpg图片格式
     */
    private static final String IMAGE_FORM_OF_JPG = "jpg";
    /**
     * png图片格式
     */
    private static final String IMAGE_FORM_OF_PNG = "png";
    
    /**
     * 水印添加时间
     */
    private static String wateraddtime = "2018-03-15 00:00:00";
  //截取的高度
  	public static int cutheight = 50;
  	
  	//截取最低高度，小于此值，不进行截取
  	public static int cutminheight = 250;
  	
  	/**
     * 源图片路径名称如
     */
    private static String srcpath = "/data/fastdfs/storage/data";

    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    //截取开关
  	public static final String watercropstatus = "open";
  	
  	//裁剪之前图片增加内容
  	public static final String _cutbefore = "_cutbefore";
  	
    
    //file存入redis的路径
  	public static final String FILE_PATH_NAME_REDIS_KEY_ = "file_path_name_redis_key_";
  	
    /**
     * 只执行一次
     */
    //@Scheduled(fixedRate = 2*60*1000)
    public void task() {
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		
    		map.put("code", "water");
    		map.put("attrkey", "watercropstatus");
    		ZxSysConfig zxSysConfig = sysConfigService.getSysConfig(map);
    		if(zxSysConfig != null && StringUtils.isNoneEmpty(zxSysConfig.getAttrvalue()) && watercropstatus.equals(zxSysConfig.getAttrvalue())){
    			//该程序只可执行一次,切记
    			map.put("attrvalue", "close");
    			boolean flag = sysConfigService.updateZxSysConfigByMap(map);
    			if(!flag){
    				return;
    			}
    			map.clear();
    			map.put("code", "water");
        		map.put("attrkey", "wateraddtime");
        		zxSysConfig = sysConfigService.getSysConfig(map);
        		if(zxSysConfig != null && StringUtils.isNotEmpty(zxSysConfig.getAttrvalue())){
        			wateraddtime = zxSysConfig.getAttrvalue();
        		}
        		map.clear();
        		map.put("code", "water");
        		map.put("attrkey", "srcpath");
        		zxSysConfig = sysConfigService.getSysConfig(map);
        		if(zxSysConfig != null && StringUtils.isNotEmpty(zxSysConfig.getAttrvalue())){
        			srcpath = zxSysConfig.getAttrvalue();
        		}
        		map.clear();
        		map.put("code", "water");
        		map.put("attrkey", "cutheight");
        		zxSysConfig = sysConfigService.getSysConfig(map);
        		if(zxSysConfig != null && StringUtils.isNoneEmpty(zxSysConfig.getAttrvalue())){
        			cutheight = Integer.parseInt(zxSysConfig.getAttrvalue());
        		}
        		map.clear();
        		map.put("code", "water");
        		map.put("attrkey", "cutminheight");
        		zxSysConfig = sysConfigService.getSysConfig(map);
        		if(zxSysConfig != null && StringUtils.isNoneEmpty(zxSysConfig.getAttrvalue())){
        			cutminheight = Integer.parseInt(zxSysConfig.getAttrvalue());
        		}
        		
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		logger.info("==========设置的执行裁剪时间(图片修改时间晚于该时间才执行)=="+wateraddtime);
        		logger.info("==========设置的执行裁剪高度=="+cutheight);
        		logger.info("==========设置的执行裁剪最低高度=="+cutminheight);
        		logger.info("==========设置的执行裁剪路径文件夹=="+srcpath);
    			ArrayList<String> filelist=getFiles(srcpath);
    			for(String filestr : filelist ){
    				logger.info(filestr);
    				
    				//只执行一次
    				String redis_value = stringRedisTemplate.opsForValue().get(FILE_PATH_NAME_REDIS_KEY_ + filestr);
    				if(StringUtils.isNotEmpty(redis_value)){
    					continue;
    				}
    				
    				//获取图片后缀
    				String suffix = getPostfix(filestr);
    				boolean isimg = getIsImg(suffix);
    				//不是图片 ,不做处理
    				if(!isimg){
    					continue;
    				}
    				
    				//裁剪前备份的原图不加水印
    				if(filestr.indexOf(_cutbefore) >=0){
    					continue;
    				}
    				Map<String, Object> imgAtrrMap = getFileAttr(filestr);
    				if(imgAtrrMap == null){
    					continue;
    				}
    				int width = imgAtrrMap.get("width")==null ? 0: Integer.parseInt(imgAtrrMap.get("width").toString());
    				int height = imgAtrrMap.get("height")==null ? 0: Integer.parseInt(imgAtrrMap.get("height").toString());
    				if(height<cutminheight){
    					continue;
    				}
    				File oldfile = new File(filestr);
    				long modifytime = oldfile.lastModified();

    				long watertime = sdf2.parse(wateraddtime).getTime();
    				if(modifytime <= watertime){//添加水印之前修改的图片不执行
    					continue;
    				}

    				String filename = oldfile.getName();
    				String pathDir = getFileParentPath(oldfile);

    				File newFile = new File(pathDir,filename.replaceAll("."+IMAGE_FORM_OF_JPG, "").replaceAll("."+IMAGE_FORM_OF_PNG, "") + _cutbefore + "." +suffix);
    				
    				
    				//已存在备份文件,则退出
    				if(newFile.exists()){
    					continue;
    				}
    				//备份文件
    				copyFileUsingJava7Files(oldfile, newFile);
    				logger.info("==========图片裁剪==="+filestr+",图片最后修改时间=="+sdf2.format(new Date(modifytime)));
    				//执行裁剪
    				boolean cropflag = this.cut(0, 0, width, height-cutheight, filestr);
    				if(cropflag){
    					stringRedisTemplate.opsForValue().set(FILE_PATH_NAME_REDIS_KEY_ + filestr,"1");
    				}
    			}
    		}
    	} catch (Exception e) {
    		logger.info("==========图片裁剪异常===");
    		e.printStackTrace();
    	}
    		
    		
    		
    }

  //获得文件的大小 返回的是M兆
 	 public static Long getFileSize(String PathName)throws Exception{
 		 File file=new File(PathName);  
 		 return file.length()/1048576;
 	 } 
 	 //获取图片的长和高
 	 public static Map<String,Object> getFileAttr(String imagePath)throws Exception{
 		 Map<String,Object> map=new HashMap<String, Object>();
 		  //读取图片对象
 		 File file = new File(imagePath);
		 if(file.length() == 0 ){
			 return null;
		 }
		 BufferedImage img = ImageIO.read(file); 
 		  //获得图片的宽
 		   int width= img.getWidth();
 		  //获得图片的高
 		  int height=img.getHeight();    
 		  map.put("width", width);
 		  map.put("height",height);
 		  return map;
 	 }


   /**
    * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
    * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
    * 
    * @param postFix
    *            文件的后缀名
    * @return
    */
   public Iterator<ImageReader> getImageReadersByFormatName(String postFix) {
       switch (postFix) {
       case IMAGE_FORM_OF_JPG:
           return ImageIO.getImageReadersByFormatName(IMAGE_FORM_OF_JPG);
       case IMAGE_FORM_OF_PNG:
           return ImageIO.getImageReadersByFormatName(IMAGE_FORM_OF_PNG);
       default:
           return ImageIO.getImageReadersByFormatName(IMAGE_FORM_OF_JPG);
       }
   }
   
   
   public static ArrayList<String> getFiles(String path) {
       ArrayList<String> files = new ArrayList<String>();
       File file = new File(path);
       File[] tempList = file.listFiles();

       for (int i = 0; i < tempList.length; i++) {
           if (tempList[i].isFile() && tempList[i].toString().indexOf("M00") < 0) {
               files.add(tempList[i].toString());
           }
           //遍历不为空的文件夹
           if (tempList[i].isDirectory() && tempList[i].listFiles().length >0 && tempList[i].toString().indexOf("M00") < 0) {
           	files.addAll(getFiles(tempList[i].toString()));
           }
       }
       return files;
   }

   /**
    * 对图片裁剪，并把裁剪完蛋新图片保存 。
    * @param srcpath 源图片路径
    * @param subpath 剪切图片存放路径
    * @throws IOException
    */
   public boolean cut(int x, int y, int width, int height, String srcpath) throws IOException {
   	
       FileInputStream is = null;
       ImageInputStream iis = null;
       try {
           // 读取图片文件
           is = new FileInputStream(srcpath);

           // 获取文件的后缀名
           String postFix = getPostfix(srcpath);
           /*
            * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
            * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
            */
           Iterator<ImageReader> it = getImageReadersByFormatName(postFix);

           ImageReader reader = it.next();
           // 获取图片流
           iis = ImageIO.createImageInputStream(is);

           /*
            * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
            * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
            */
           reader.setInput(iis, true);

           /*
            * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
            * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
            * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
            */
           ImageReadParam param = reader.getDefaultReadParam();

           /*
            * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
            * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
            */
           Rectangle rect = new Rectangle(x, y, width, height);

           // 提供一个 BufferedImage，将其用作解码像素数据的目标。
           param.setSourceRegion(rect);
           /*
            * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
            * BufferedImage 返回。
            */
           BufferedImage bi = reader.read(0, param);
           
           // 保存新图片
           ImageIO.write(bi, postFix, new File(srcpath));
           return true;
       } catch(Exception e){
    	   return false;
       } finally {
           if (is != null)
               is.close();
           if (iis != null)
               iis.close();
       }

   }

   /**
    * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"<br>
    * 
    * @param inputFilePath
    * @return
    */
   public static String getPostfix(String inputFilePath) {
       return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
   }
   
   /**
	 * 是否是图片
	 * @param ext
	 * @return "jpg", "jpeg", "gif", "png", "bmp" 为文件后缀名者为图片
	 */
	public static boolean getIsImg(String ext) {
		ext = ext.toLowerCase();
		for (String s : IMAGE_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}
	
	
	private static void copyFileUsingJava7Files(File source, File dest)
	         throws IOException {    
	         Files.copy(source.toPath(), dest.toPath());
	 }
    
   /**
    * 获取文件的父路径
    * @param file
    * @return
    * @throws Exception
    */
   private static String getFileParentPath(File file) throws Exception{
       if(file == null || !file.exists()){
           throw new Exception("文件不存在，请检查！");
       }
       String path = file.getAbsolutePath();
       int len = path.lastIndexOf("/");
       return path.substring(0,len);
   }
}
