package com.sharmila.musiclibrary.utils;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sharmila.musiclibrary.utils.ConfigUtils;

@Component
@PropertySource("classpath:config.properties")

public class ConfigUtils {
	
	@Autowired
	private Environment env;
	@Value("${esHost}")
	private  String esHost;
	@Value("${esCluster}")
	private  String esCluster;
	@Value("${esGlobalUser}")
	private  String esGlobalUser;
	@Value("${esGlobalUserPass}")
	private  String esGlobalUserPass;
	@Value("${esPort}")
	private  int esPort;
	
	@Value("${server.port}")
	private int sport;
	
	
	public ConfigUtils()
	{
		
		
	}


	
	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public String getEsHost() {
		System.out.println("///////"+esHost);
		return env.getProperty("esHost");
	}

	public void setEsHost(String esHost) {
		this.esHost = esHost;
	}

	public String getEsCluster() {
		return esCluster;
	}

	public void setEsCluster(String esCluster) {
		this.esCluster = esCluster;
	}

	public String getEsGlobalUser() {
		return esGlobalUser;
	}

	public void setEsGlobalUser(String esGlobalUser) {
		this.esGlobalUser = esGlobalUser;
	}

	public String getEsGlobalUserPass() {
		return esGlobalUserPass;
	}

	public void setEsGlobalUserPass(String esGlobalUserPass) {
		this.esGlobalUserPass = esGlobalUserPass;
	}

	public int getEsPort() {
		return esPort;
	}

	public void setEsPort(int esPort) {
		this.esPort = esPort;
	}

	public int getSport() {
		return sport;
	}

	public void setSport(int sport) {
		this.sport = sport;
	}

	

	
	
	
}
