package com.sharmila.musiclibrary.repository;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;


import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;

import org.elasticsearch.common.unit.TimeValue;

import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.sort.SortOrder;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharmila.musiclibrary.api.domain.Music;
import com.sharmila.musiclibrary.esclient.ElasticSearch5xClient;


@Component
public class MusicRepository {

	//private static final Logger logger = LoggerFactory.getLogger(MusicRepository.class);

	
	private static Client client = ElasticSearch5xClient.CLIENT.getInstance();
	
	public static Map<String, Object> sourceMap = new HashMap<String, Object>();

	

	
	public RestStatus create(Music music) {
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		RestStatus s=null;
		// generate json
		try {
			byte[] json = mapper.writeValueAsBytes(music);
			System.out.println(json);
			
			IndexResponse indexResponse=	client.prepareIndex("musiclibrary", "music").setSource(json).execute().actionGet();
			 s = indexResponse.status();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public RestStatus delete(String id) {
		System.out.println("----" + id);
		
		DeleteResponse response = client.prepareDelete("musiclibrary", "music", id).execute().actionGet();
		
		return response.status();
	}

	public RestStatus update(Music music,String id) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		RestStatus res=null;
		try {
			byte[] json = mapper.writeValueAsBytes(music);
		
			IndexRequest indexRequest = new IndexRequest("musiclibrary",  "music", id)
			        .source(jsonBuilder()
			            .startObject()
			            .field("singer", music.getSinger())
						.field("composer", music.getComposer())
						.field("title", music.getTitle())
						.field("modifiedDate",music.getModifiedDate())
			            .endObject());
			UpdateRequest updateRequest = new UpdateRequest("musiclibrary",  "music", id)
			        .doc(jsonBuilder()
			            .startObject()
			            .field("singer", music.getSinger())
						.field("composer", music.getComposer())
						.field("title", music.getTitle())
						.field("modifiedDate",music.getModifiedDate())
			            .endObject())
			        .upsert(indexRequest);   
			
			
			
//			UpdateRequest updateRequest = new UpdateRequest("musiclibrary", "music", id)
//					.doc(jsonBuilder().startObject()
//							.field("singer", music.getSinger())
//							.field("composer", music.getComposer())
//							.field("title", music.getTitle())
//							.field("modifiedDate",music.getModifiedDate())
//							.endObject());
			UpdateResponse response = client.update(updateRequest).get();
			
		res=response.status();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public GetResponse   getById(String id) {

		
		GetResponse response = client.prepareGet("musiclibrary", "music", id).execute()
				.actionGet();
		
		
		
		return response;
	}

	

	public List<Map<String, Object>> searchAll(String sortBy,String sortOrder,int size,int from) {
		
		SearchResponse response=null;
		SortOrder srtOrder;
		if(sortOrder.equalsIgnoreCase("ASC")){
			srtOrder = SortOrder.ASC;
		}
	
		else{
			srtOrder=SortOrder.DESC;
		}
		
		System.out.println("Repository---->> sort by "+sortBy +" sort order "+sortOrder + " size "+size +" from "+from );
		

			response = client.prepareSearch("musiclibrary").setTypes("music")
					.addSort(sortBy,srtOrder)	
					.setSize(size)
					.setFrom(from)
					.execute().actionGet();
	
			System.out.println(response.getHits().getTotalHits());
		SearchHit[] searchHits = response.getHits().getHits();
		List<Map<String, Object>> mapList = new ArrayList<>();

		for (SearchHit s : searchHits) {
			System.out.println();
			mapList.add(s.getSource());
			for(Map.Entry<String, Object> e:s.getSource().entrySet()){
				System.out.println(e.getKey() + " "+e.getValue());
			}
			sourceMap.put("source", mapList);

		}
		
		return mapList;

	}

	
	@JsonProperty("parameters")
	@XmlElement(required = true)
	public void bulkTest(List<Music> music) {
	
		ObjectMapper mapper = new ObjectMapper();

		try {
			byte[] json = mapper.writeValueAsBytes(music);
			BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {

				@Override
				public void beforeBulk(long executionId, BulkRequest request) {
					System.out.println("---- before bulk");

				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
					System.out.println("---- after bulk");

				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
					System.out.println("---- after 1 bulk");

				}
			}).setBulkActions(1000)

					.setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(1)
					
					.build();

			bulkProcessor.add(new IndexRequest("musiclibrary", "music").source(json));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
