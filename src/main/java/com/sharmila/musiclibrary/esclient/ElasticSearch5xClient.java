package com.sharmila.musiclibrary.esclient;

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






public class ElasticSearch5xClient {


	// final Logger logger = LogManager.getLogger(Logger.class.getName());
	private TransportClient client;

	

	

	private ElasticSearch5xClient() {
		
		// logger.info("Creating new elasticsearch 5.X client object...");
	
//		String esCluster = ConfigUtils.getProperty("esCluster");
//		String esGlobalUser=ConfigUtils.getProperty("esGlobalUser");
//		String esGlobalUserPassword=ConfigUtils.getProperty("esGlobalUserPass");
//		
//		int esPort = Integer.parseInt(ConfigUtils.getProperty("esPort"));
//
//		
//		//In order to use TransportClient in X-pack, the user needs to have transport_client role in cluster privilege
//		
//		Settings settings = Settings.builder().put("cluster.name", esCluster)
//				.put("xpack.security.user", esGlobalUser+":"+esGlobalUserPassword)
//				.put("client.transport.ping_timeout", 7200, TimeUnit.SECONDS).build();
//		try {
//			client = new PreBuiltXPackTransportClient(settings)
//					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost1), esPort));
//				
//
//			
//
//		} catch (UnknownHostException e) {
//			// logger.info("{}",e.getMessage());
//		}

	}

	public Client getInstance() {
		//System.out.println("-testing--"+configUtils.getEsCluster());
		return this.client;
	}

	public void destory() {
		this.client.close();
	}
}
