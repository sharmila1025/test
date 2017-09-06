package com.sharmila.musiclibrary.esclient;

import static org.elasticsearch.xpack.security.authc.support.UsernamePasswordToken.basicAuthHeaderValue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.security.authc.support.SecuredString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties
public class ElasticSearch5xClient {

	
	private  String esHost;
	
	private  String esCluster;
	
	private  String esGlobalUser;
	
	private  String esGlobalUserPass;

	private  int esPort;
	
	
	public   TransportClient transportClient;
	
	public static  Client  client;
	
	@SuppressWarnings("resource")
	@PostConstruct
	public void init() throws Exception {

    	
    	System.out.println( " the "+esCluster);
		
    	Settings settings = Settings.builder().put("cluster.name",esCluster )
		.put("xpack.security.user", esGlobalUser+":"+esGlobalUserPass)
		.put("client.transport.ping_timeout", 7200, TimeUnit.SECONDS).build();
		try {
			transportClient = new PreBuiltXPackTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
				
		
			System.out.println(" the client in the init method "+transportClient);
			
		} catch (UnknownHostException e) {
			System.out.println(" error in  "+e);
		}
	
	
    }
	
	public Client getClient(String username,String password) throws Exception
	{
		client=transportClient;
		System.out.println("before auth "+transportClient);
		String token = basicAuthHeaderValue(username, new SecuredString(password.toCharArray()));
		
		client=transportClient.filterWithHeader(Collections.singletonMap("Authorization", token));
		System.out.println(" after auth "+client);
		return client;
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

	public TransportClient getTransportClient() {
		return transportClient;
	}

	public void setTransportClient(TransportClient transportClient) {
		this.transportClient = transportClient;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		ElasticSearch5xClient.client = client;
	}

	
}
