package com.water.component.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by pc on 2017/9/14.
 */
@Component
public class Properties {
	
	@Value("${default.user.img.url}")
    private String defaultUserImgUrl;

	
	@Value("${bbs.lucene.url}")
    private String bbsLuceneUrl;
	
	
	@Value("${bbs.user.img.download.url}")
    private String bbsUserImgDownloadUrl;




	public String getDefaultUserImgUrl() {
		return defaultUserImgUrl;
	}


	public void setDefaultUserImgUrl(String defaultUserImgUrl) {
		this.defaultUserImgUrl = defaultUserImgUrl;
	}


	public String getBbsLuceneUrl() {
		return bbsLuceneUrl;
	}


	public void setBbsLuceneUrl(String bbsLuceneUrl) {
		this.bbsLuceneUrl = bbsLuceneUrl;
	}


	public String getBbsUserImgDownloadUrl() {
		return bbsUserImgDownloadUrl;
	}


	public void setBbsUserImgDownloadUrl(String bbsUserImgDownloadUrl) {
		this.bbsUserImgDownloadUrl = bbsUserImgDownloadUrl;
	}

}
