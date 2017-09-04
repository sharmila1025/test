package com.sharmila.musiclibrary.utils;


import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.sharmila.musiclibrary.utils.ConfigUtils;

@Component
//@PropertySource("classpath:config.properties")
@ConfigurationProperties
public class ConfigUtils {
	
	
	private  String esHost;
	
	private  String esCluster;
	
	private  String esGlobalUser;
	
	private  String esGlobalUserPass;

	private  int esPort;
	
	
	
	public static TransportClient client;
	
	@Bean
    public Client client() throws Exception {

		Settings settings = Settings.builder().put("cluster.name", esCluster)
		.put("xpack.security.user", esGlobalUser+":"+esGlobalUserPass)
		.put("client.transport.ping_timeout", 7200, TimeUnit.SECONDS).build();
		try {
			client = new PreBuiltXPackTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
				
		
			
		
		} catch (UnknownHostException e) {
			// logger.info("{}",e.getMessage());
		}
		return null;
    }

   

	public String getEsHost() {
		return esHost;
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
	
	

}
