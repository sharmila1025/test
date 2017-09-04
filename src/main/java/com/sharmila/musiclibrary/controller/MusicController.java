package com.sharmila.musiclibrary.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharmila.musiclibrary.api.MusicManager;
import com.sharmila.musiclibrary.api.domain.Music;
import com.sharmila.musiclibrary.utils.ConfigUtils;


@RestController
@RequestMapping(value="/music")
public class MusicController {
	
	@Autowired
	private MusicManager musicManager;
	
	private ConfigUtils configUtils;
	
	@Autowired
	public void setConfigUtils(ConfigUtils configUtils) {
		this.configUtils = configUtils;
	}
	private  List<Map<String,Object>> response=new ArrayList<>();
	@RequestMapping(method=RequestMethod.GET)
	public List<Map<String,Object>> search(
			@RequestParam(value="sortBy",required=false)String sortBy,
			@RequestParam(value="sortOrder",required=false)String sortOrder,
			@RequestParam(value="size",required=false)Integer size,
			@RequestParam(value="page",required=false)Integer page,
			@RequestParam(value="role",required=false)String role){
		System.out.println("++++"+ configUtils.getEsCluster());
		Integer from=1;
		System.out.println("recieved size "+size);
		if(sortBy==null){
			sortBy="modifiedDate";
		}
		if(sortOrder==null){
			sortOrder="DESC";
		}
		
		if(size==null && page==null){
			size=10;
			from=0;
		}
		if(page!=null){
			from=(page-1)*size;
		}
		else{
			from=0;
		}
		
		System.out.println("sort by "+sortBy +" sort order "+sortOrder + " size "+size +" from "+from );
		response = musicManager.searchAll(sortBy, sortOrder, size, from);


		return response;
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST)
	public RestStatus createIndex(@RequestBody Music music){
		
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
