package com.sharmila.musiclibrary.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.rest.RestStatus;

import com.sharmila.musiclibrary.api.domain.Music;


public interface MusicManager {
	
	RestStatus create(Music music);
	RestStatus delete(String id);
	RestStatus update(Music music,String id) throws InterruptedException, ExecutionException, IOException ;
	GetResponse	  getById(String id);
	BulkResponse bulkOperation(Object obj);
	List<Map<String,Object>>  searchAll(String sortBy,String sortOrder,String size,String from);
	
}
