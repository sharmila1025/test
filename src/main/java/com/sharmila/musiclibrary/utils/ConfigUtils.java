package com.sharmila.musiclibrary.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:userconfig.properties")
@ConfigurationProperties
public class ConfigUtils {

	private String userNepal; 
	private String userNepalPass;
	
	private String userUs;
	private String userUsPass;
	
	
	public String getUserNepal() {
		return userNepal;
	}
	public void setUserNepal(String userNepal) {
		this.userNepal = userNepal;
	}
	public String getUserNepalPass() {
		return userNepalPass;
	}
	public void setUserNepalPass(String userNepalPass) {
		this.userNepalPass = userNepalPass;
	}
	public String getUserUs() {
		return userUs;
	}
	public void setUserUs(String userUs) {
		this.userUs = userUs;
	}
	public String getUserUsPass() {
		return userUsPass;
	}
	public void setUserUsPass(String userUsPass) {
		this.userUsPass = userUsPass;
	}
	
	
}
