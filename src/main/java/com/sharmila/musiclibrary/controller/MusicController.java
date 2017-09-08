package com.sharmila.musiclibrary.controller;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.action.DocWriteResponse.Result;
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
	
	
	
	@Autowired
	private MusicRepository musicRepository;
	

	
	
	@Autowired
	private ElasticSearch5xClient elasticSearch5xClient;
	
	private  Client client ;
	
	



	private  List<Map<String,Object>> response=new ArrayList<>();
	@RequestMapping(method=RequestMethod.GET)
	public List<Map<String,Object>> search(
			@RequestParam(value="sortBy",required=false)String sortBy,
			@RequestParam(value="sortOrder",required=false)String sortOrder,
			@RequestParam(value="size",required=false)String size,
			@RequestParam(value="page",required=false)String page,
			@RequestParam(value="role",required=true)String role) throws Exception {
		

		
		elasticSearch5xClient.sendAuthReq(role);
		response = musicManager.searchAll(sortBy, sortOrder, size, page);
		

		return response;
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST)
	public RestStatus createIndex(@RequestBody Music music,@RequestParam(value="role",required=true)String role) throws Exception{
		elasticSearch5xClient.sendAuthReq(role);
		
		RestStatus response=musicManager.create(music);
		return response;
	}
	

	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public RestStatus update(@RequestBody Music music,@RequestParam(value="role",required=true)String role,@PathVariable(value="id")String id)throws Exception{
		
		elasticSearch5xClient.sendAuthReq(role);
		return musicManager.update(music, id); 
		
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public GetResponse  getById(@PathVariable(value="id")String id,@RequestParam(value="role",required=true)String role)throws Exception{
		elasticSearch5xClient.sendAuthReq(role);
		GetResponse	 response=musicManager.getById(id);
		System.out.println(response);
		return response;
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public RestStatus deleteIndexItems(@PathVariable(value="id")String id,@RequestParam(value="role",required=true)String role)throws Exception{
		elasticSearch5xClient.sendAuthReq(role);
		System.out.println("delete api called : id"+id);
		RestStatus response=musicManager.delete(id);
		
		return response;
	}
	
	
	
	
}
