package com.water.schedule;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.water.modules.model.ZxSysConfig;
import com.water.service.SysConfigService;

/**
 * 资讯随机增加水印程序
 */
//@Component
//@EnableScheduling
public class WaterAddNoFixTask {
    private static Logger logger = LoggerFactory.getLogger(WaterAddNoFixTask.class);

    
    
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
  
    //截取的高度
  	public static int cutheight = 50;
  	
  	//截取最低高度，小于此值，不进行截取
  	public static int cutminheight = 250;
  	
  	/**
     * 源图片路径名称如
     */
    private static String srcpath = "d:/workspace2018/images/";
    
    
    /**
     * 水印源图片路径名称如
     */
    private static String watersrcpath = "d:/water.png";

    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
   //随机添加水印开关
  	public static final String waternofixaddstatus = "open";
  	
  	//裁剪之前图片增加内容
  	public static final String _cutbefore = "_cutbefore";
  	
  	//添加水印之前图片增加内容
  	public static final String _waterbefore = "_waterbefore";
  	
    /**
     * 五分钟执行一次,定时执行水印前的原图-----水印固定,不执行此定时任务
     */
    @Scheduled(fixedRate = 1000*60*5)
    public void task() {
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		
    		map.put("code", "water");
    		map.put("attrkey", "waternofixaddstatus");
    		ZxSysConfig zxSysConfig = sysConfigService.getSysConfig(map);
    		if(zxSysConfig != null && StringUtils.isNoneEmpty(zxSysConfig.getAttrvalue()) && waternofixaddstatus.equals(zxSysConfig.getAttrvalue())){
    			/*map.put("attrvalue", "close");
    			boolean flag = sysConfigService.updateZxSysConfigByMap(map);
    			if(!flag){
    				return;
    			}*/
    			
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
        		
        		map.clear();
        		map.put("code", "water");
        		map.put("attrkey", "watersrcpath");
        		zxSysConfig = sysConfigService.getSysConfig(map);
        		if(zxSysConfig != null && StringUtils.isNoneEmpty(zxSysConfig.getAttrvalue())){
        			watersrcpath = zxSysConfig.getAttrvalue();
        		}
        		
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        		logger.info("==========水印随机设置的执行高度=="+cutheight);
        		logger.info("==========水印随机设置的执行裁剪最低高度=="+cutminheight);
        		logger.info("==========水印随机设置的执行路径文件夹=="+srcpath);
    			ArrayList<String> filelist=getFiles(srcpath);
    			for(String filestr : filelist ){
    				//只执行一次
    				
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
    				
    				//只执行水印前的原图片
    				if(filestr.indexOf(_waterbefore) <0){
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
    				
    				
    				String filename = oldfile.getName();
    				String pathDir = getFileParentPath(oldfile);
    				File newFile = new File(pathDir,filename.replaceAll("."+IMAGE_FORM_OF_JPG, "").replaceAll("."+IMAGE_FORM_OF_PNG, "").replaceAll(_waterbefore, "")  + "." +suffix);
    				logger.info("==========图片随机水印增加==="+newFile.toString());
    				//执行添加水印----增加水印的文件名去掉了_waterbefore
    				//this.mark(filestr, watersrcpath, newFile.toString(), 100, 27, getRandom(0, width-100), getRandom(0,height-27)); 
    				this.mark(filestr, watersrcpath, newFile.toString(), 100, 27, width-110, height-30);
    			}
    		}
    	} catch (Exception e) {
    		logger.info("==========图片随机加水印异常===");
    		e.printStackTrace();
    	}
    		
    		
    		
    }

    /**
     * 给图片增加图片水印
     * 
     * @param inputImg
     *            -源图片，要添加水印的图片
     * @param markImg
     *            - 水印图片
     * @param outputImg
     *            -输出图片(可以是源图片)
     * @param width
     *            - 水印图片宽度
     * @param height
     *            -水印图片高度
     * @param x
     *            -横坐标，相对于源图片
     * @param y
     *            -纵坐标，同上
     */ 
    public boolean mark(String inputImg, String markImg, String outputImg, int width, int height, int x, int y) { 
        // 读取原图片信息 
        File inputImgFile = null; 
        File markImgFile = null; 
        Image img = null; 
        Image mark = null; 
        try { 
            if (inputImg != null && markImg != null) { 
                inputImgFile = new File(inputImg); 
                markImgFile = new File(markImg); 
            } 
            if (inputImgFile != null && inputImgFile.exists() && inputImgFile.isFile() && inputImgFile.canRead()) { 
   
                img = ImageIO.read(inputImgFile); 
   
            } 
            if (markImgFile != null && markImgFile.exists() && markImgFile.isFile() && markImgFile.canRead()) { 
   
                mark = ImageIO.read(markImgFile); 
   
            } 
            int imgWidth = img.getWidth(null); 
            int imgHeight = img.getHeight(null); 
            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB); 
            mark(bufImg, img, mark, width, height, x, y); 
            FileOutputStream outImgStream = new FileOutputStream(outputImg); 
            ImageIO.write(bufImg, "jpg", outImgStream); 
            outImgStream.flush(); 
            outImgStream.close();
            return true;
        } catch (Exception e) {
        	return false;
        } 
    }
    
    // 加图片水印 
    public void mark(BufferedImage bufImg, Image img, Image markImg, int width, int height, int x, int y) { 
        Graphics2D g = bufImg.createGraphics(); 
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null); 
        g.drawImage(markImg, x, y, width, height, null); 
        g.dispose(); 
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
    //如果文件夹不存在则创建    
      if  (!file .exists()  && !file .isDirectory())      
      {       
          System.out.println("//不存在");  
          file .mkdir();    
      }
      File[] tempList = file.listFiles();

      for (int i = 0; i < tempList.length; i++) {
          if (tempList[i].isFile()) {
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
      int len = path.lastIndexOf("\\");//线上需要改为int len = path.lastIndexOf("/");
       
      return path.substring(0,len);
  }
  
  public static int getRandom(int min, int max){
	    Random random = new Random();
	    int s = random.nextInt(max) % (max - min + 1) + min;
	    return s;

	}
}
