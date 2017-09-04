This is simple spring boot project with elastic search using java api 1.7


Creating Client in Elastic search
	Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true)
				.put("number_of_shards", 3).put("node.client", true)

				.put("client.transport.ping_timeout", "60s").put("cluster.name", clusterName).build();
		// TransportClient client = new TransportClient(settings);
		@SuppressWarnings("resource")
		Client client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));


Here the TransportClient is used , '9300' is the specific port used because '9200' are used by httpmethods when transport clients are used.


CRUD OPERATIONS	

1. CREATE
 A document is created in ES by using 

IndexResponse response=	client.prepareIndex("musiclibrary", "music").setSource(json).execute().actionGet();

Here "musiclibrary" and "music" are index and type respectively. 


2. RETRIEVE

	i) GetbyId
	-GetResponse response = client.prepareGet("musiclibrary", "music", id).execute()
				.actionGet();

	Id is passed to the prepareGet method and response will give the source if the id exists.



	ii)Search all
	-response = client.prepareSearch("musiclibrary").setTypes("music")
					.addSort(sortBy,srtOrder)	
					.setSize(size)
					.setFrom(from)
					.execute().actionGet();


	The sortBy,sortOrder ,size and from are passed to the prepareSearch method.
	The sortBy is the fieldName , sortOrder is the ascending or descending order.
	The size and from are size per page in the front end to view the data.

3. DELETE
	DeleteResponse response = client.prepareDelete("musiclibrary", "music", id).execute().actionGet();


	
4. UPDATE
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



	Here the update is done using upsert method. When upsert method is used, if the 'id' doesnt exist then the document is created with 
the id .

	
