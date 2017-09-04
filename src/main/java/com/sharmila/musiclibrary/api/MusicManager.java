package com.sharmila.musiclibrary.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.rest.RestStatus;

import com.sharmila.musiclibrary.api.domain.Music;


public interface MusicManager {
	
	RestStatus create(Music music);
	RestStatus delete(String id);
	RestStatus update(Music music,String id) throws IOException;
	GetResponse	  getById(String id);
	void bulkTest(List<Music> music);
	List<Map<String,Object>>  searchAll(String sortBy,String sortOrder,int size,int from);
	
}
