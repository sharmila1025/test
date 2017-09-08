package com.sharmila.musiclibrary.controller;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharmila.musiclibrary.api.MusicManager;
import com.sharmila.musiclibrary.api.domain.Music;
import com.sharmila.musiclibrary.esclient.ElasticSearch5xClient;
import com.sharmila.musiclibrary.utils.ConfigUtils;

@RestController
@RequestMapping(value="/bulk")
public class BulkController {
 
	@Autowired
	private MusicManager musicManager;
	private ConfigUtils configUtils;
	@Autowired
	private ElasticSearch5xClient elasticSearch5xClient;
	
	private  Client client ;
	
	
	@Autowired
	public void setConfigUtils(ConfigUtils configUtils) {
		this.configUtils = configUtils;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public RestStatus bulkReq(Object obj, @RequestParam(value = "role", required = true) String role)
			throws Exception{
			sendAuthReq(role);
		// musicManager.bulkTest(obj);
		
		return null;
	}
	
	public void sendAuthReq(String role) throws Exception{
		if(role.equalsIgnoreCase("usteam")){
			
			String  username=configUtils.getUserUs();
			String password=configUtils.getUserUsPass();
			
			System.out.println(" user name "+username);
			System.out.println(" password  "+password);
			elasticSearch5xClient.checkAuth(username, password);
			
			
			
		}
		else if(role.equalsIgnoreCase("nepalteam")){
			
			String  username=configUtils.getUserNepal();
			String password=configUtils.getUserNepalPass();
			
			System.out.println(" user name "+username);
			System.out.println(" password  "+password);
			elasticSearch5xClient.checkAuth(username, password);
		}
		else{
			
			elasticSearch5xClient.checkAuth("elastic", "elastic123");
			System.out.println(" the role is "+role);
		}
	}
}
