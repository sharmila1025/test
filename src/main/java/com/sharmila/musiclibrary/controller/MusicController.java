package com.sharmila.musiclibrary.controller;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xpack.security.authc.support.SecuredString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharmila.musiclibrary.api.MusicManager;
import com.sharmila.musiclibrary.api.domain.Music;
import com.sharmila.musiclibrary.esclient.ElasticSearch5xClient;
import com.sharmila.musiclibrary.repository.MusicRepository;
import com.sharmila.musiclibrary.utils.ConfigUtils;
import static org.elasticsearch.xpack.security.authc.support.UsernamePasswordToken.basicAuthHeaderValue;



@RestController
@RequestMapping(value="/music")

public class MusicController {
	
	@Autowired
	private MusicManager musicManager;
	
	private ConfigUtils configUtils;
	
	@Autowired
	private MusicRepository musicRepository;
	

	
	
	@Autowired
	private ElasticSearch5xClient elasticSearch5xClient;
	
	private  Client client ;
	
	
	@Autowired
	public void setConfigUtils(ConfigUtils configUtils) {
		this.configUtils = configUtils;
	}



	private  List<Map<String,Object>> response=new ArrayList<>();
	@RequestMapping(method=RequestMethod.GET)
	public List<Map<String,Object>> search(
			@RequestParam(value="sortBy",required=false)String sortBy,
			@RequestParam(value="sortOrder",required=false)String sortOrder,
			@RequestParam(value="size",required=false)String size,
			@RequestParam(value="page",required=false)String page,
			@RequestParam(value="role",required=true)String role) throws Exception {
		
		if(role.equalsIgnoreCase("usteam")){
		
			String  username=configUtils.getUserUs();
			String password=configUtils.getUserUsPass();
			
			System.out.println(" user name "+username);
			System.out.println(" password  "+password);
			elasticSearch5xClient.getClient(username, password);
			
			
			
		}
		else if(role.equalsIgnoreCase("nepalteam")){
			
			String  username=configUtils.getUserUs();
			String password=configUtils.getUserUsPass();
			elasticSearch5xClient.getClient(username, password);
		}
		else{
			response=null;
			elasticSearch5xClient.getClient("aa", "bb");
			System.out.println(" the role is "+role);
		}
		
		
		
		
		//System.out.println("sort by "+sortBy +" sort order "+sortOrder + " size "+size +" from "+from );
		response = musicManager.searchAll(sortBy, sortOrder, size, page);
		

		return response;
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST)
	public RestStatus createIndex(@RequestBody Music music){
		
		RestStatus response=musicManager.create(music);
		return response;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public RestStatus update(@RequestBody Music music){
		
		RestStatus response=musicManager.create(music);
		return response;
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public GetResponse  getById(@PathVariable(value="id")String id){
	
		GetResponse	 response=musicManager.getById(id);
		System.out.println(response);
		return response;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public RestStatus deleteIndexItems(@PathVariable(value="id")String id){
		System.out.println("delete api called : id"+id);
		RestStatus response=musicManager.delete(id);
		
		return response;
	}
	
	
	
}
