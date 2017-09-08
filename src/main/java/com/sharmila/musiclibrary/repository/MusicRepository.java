package com.sharmila.musiclibrary.repository;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.xpack.security.authc.support.UsernamePasswordToken.basicAuthHeaderValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlElement;

import org.elasticsearch.ElasticsearchSecurityException;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.RemoteTransportException;
import org.elasticsearch.xpack.notification.email.Attachment.XContent.Json;
import org.elasticsearch.xpack.security.authc.support.SecuredString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharmila.musiclibrary.api.domain.Music;
import com.sharmila.musiclibrary.controller.MusicController;
import com.sharmila.musiclibrary.esclient.ElasticSearch5xClient;

@Component
public class MusicRepository {

	@Autowired
	private ElasticSearch5xClient elasticSearch5xClient;

	private Client client;
	public static Map<String, Object> sourceMap = new HashMap<String, Object>();

	public static final String indexName = "company";
	public static final String typeName = "music";

	private RestStatus res;

	@Autowired
	public void setElasticSearch5xClient(ElasticSearch5xClient elasticSearch5xClient) {
		this.elasticSearch5xClient = elasticSearch5xClient;
	}

	public RestStatus delete(String id) {
		client = elasticSearch5xClient.getClient();
		DeleteResponse response=null;
		RestStatus restStatus=null;;
		System.out.println("----" + id);
		try{
		 response = client.prepareDelete(indexName, typeName, id).execute().actionGet();
		 
		}catch (Exception e) {
			restStatus = restStatus.UNAUTHORIZED;
			return restStatus;
		}
		return response.status();
	}

	public RestStatus create(Music music) {
		client = elasticSearch5xClient.getClient();
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		RestStatus restStatus = null;
		// generate json
		IndexResponse indexResponse=null;
			byte[] json;
			try {
				json = mapper.writeValueAsBytes(music);
				System.out.println(json);
				 indexResponse = client.prepareIndex(indexName, typeName).setSource(json).execute()
						.actionGet();
				restStatus = indexResponse.status();
			} catch (Exception e) {
				restStatus = restStatus.UNAUTHORIZED;
				if (e.getCause() instanceof JsonProcessingException) {
					restStatus = restStatus.INTERNAL_SERVER_ERROR;
					if (indexResponse==null) {
						restStatus = restStatus.INTERNAL_SERVER_ERROR;
					}

					e.printStackTrace();
					return restStatus;
				}
			
			e.printStackTrace();
			
			}
		System.out.println(" reststatus " + restStatus);
		return restStatus;
	}

	public RestStatus update(Music music, String id) {
		System.out.println("before calling client in update");
		client = elasticSearch5xClient.getClient();
		System.out.println("update in repo");
		ObjectMapper mapper = new ObjectMapper();
		RestStatus restStatus = null;
		UpdateRequest updateRequest = null;
		UpdateResponse updateResponse = null;

		IndexRequest indexRequest;
		try {
			byte[] json = mapper.writeValueAsBytes(music);
			indexRequest = new IndexRequest(indexName, typeName, id).source(jsonBuilder().startObject()
					.field("singer", music.getSinger()).field("composer", music.getComposer())
					.field("title", music.getTitle()).field("modifiedDate", music.getModifiedDate()).endObject());
			updateRequest = new UpdateRequest(indexName, typeName, id).doc(jsonBuilder().startObject()
					.field("singer", music.getSinger()).field("composer", music.getComposer())
					.field("title", music.getTitle()).field("modifiedDate", music.getModifiedDate()).endObject())
					.upsert(indexRequest);

			updateResponse = client.update(updateRequest).get();
			System.out.println(" ---- the client " + client);
			restStatus=updateResponse.status();
		} catch (Exception e) {
			if (e.getCause() instanceof ExecutionException) {
				
				if (updateResponse==null) {
					restStatus = restStatus.UNAUTHORIZED;
				}

				e.printStackTrace();
				return restStatus;

			} else if (e.getCause() instanceof RemoteTransportException) {
				
				if (updateResponse==null) {
					restStatus = restStatus.UNAUTHORIZED;
				}

				e.printStackTrace();
				return restStatus;

			}
			if (e.getCause() instanceof ElasticsearchSecurityException) {
				if (updateResponse==null) {
					restStatus = restStatus.UNAUTHORIZED;
				}

				e.printStackTrace();
				return restStatus;
			}

		}
		return restStatus;

	}

	public GetResponse getById(String id) {

		client = elasticSearch5xClient.getClient();
		GetResponse response = client.prepareGet(indexName, typeName, id).execute().actionGet();

		return response;
	}

	public List<Map<String, Object>> searchAll(String sortBy, String sortOrder, String size, String page) {

		client = elasticSearch5xClient.getClient();
		SearchResponse response = null;
		SortOrder srtOrder = SortOrder.DESC;
		;
		int dataSize;
		int frontPage;

		Integer from = 0;

		if (size == null) {
			dataSize = 10;
		} else {
			dataSize = Integer.parseInt(size);
		}

		if (page == null) {
			frontPage = 0;
		} else {
			frontPage = Integer.parseInt(page);
		}

		if (dataSize == 0 && frontPage == 0) {
			dataSize = 10;
			from = 0;
		}
		if (frontPage > 0) {
			from = (frontPage - 1) * dataSize;
		} else {
			from = 0;
		}

		if (sortBy == null) {
			sortBy = "modifiedDate";
		}

		if (sortOrder == null) {
			srtOrder = SortOrder.DESC;
		} else {
			if (sortOrder.equalsIgnoreCase("ASC")) {
				srtOrder = SortOrder.ASC;
			}

			else {
				srtOrder = SortOrder.DESC;
			}
		}

		// if sorting on fields which is not date type then we need to use
		// fieldname.keyword because field type is by default text

		System.out.println(
				"Repository---->> sort by " + sortBy + " sort order " + sortOrder + " size " + size + " from " + from);

		System.out.println(" client is " + client);
		response = client.prepareSearch("company").setTypes("music").addSort(sortBy, srtOrder).setSize(dataSize)
				.setFrom(from).execute().actionGet();

		System.out.println(response.getHits().getTotalHits());
		SearchHit[] searchHits = response.getHits().getHits();
		List<Map<String, Object>> mapList = new ArrayList<>();

		for (SearchHit s : searchHits) {
			System.out.println();
			mapList.add(s.getSource());
			for (Map.Entry<String, Object> e : s.getSource().entrySet()) {
				System.out.println(e.getKey() + " " + e.getValue());
			}
			sourceMap.put("source", mapList);

		}

		return mapList;

	}

	@JsonProperty("parameters")
	@XmlElement(required = true)
	public BulkResponse bulkTest(List<Object> obj) {
		client = elasticSearch5xClient.getClient();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		// logger.debug("Bulk index {} objects", objects.length);
		for (Object object : obj) {
			if (object != null) {

			}
		}
		return bulkRequest.get();

	}
}
