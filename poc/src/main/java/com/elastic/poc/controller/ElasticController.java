package com.elastic.poc.controller;

import java.awt.List;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.elastic.poc.User;
import com.elastic.poc.Configuration.Config;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@RestController
public class ElasticController {
	

	//Declaring searchvariables
	final private static String[] FETCH_FIELDS = { "@timestamp", "@message" };

	final private static String MATCH_FIELD = "@message";
	final private static String[] MUST_MATCH = { "brewster-shaw", "AppleWebKit" };
	final private static String[] MUST_NOT_MATCH = { "21.211.33.63" };

	final private static String TIME_FIELD = "@timestamp";
	final private static String START_TIME = "2015-05-20T13:06:50";
	final private static String END_TIME = "2025-05-06T00:00:00";

	final private static String INDEX = "logstash-2015.05.20"; // accepts * as wildcard, .e.g log*


	
	//declaring config bean
	@Autowired Config restClient;
   
//@PostMapping("/create")
//    public String create(@RequestBody User user) throws IOException {
//        IndexRequestBuilder builder = client.prepareIndex("users", "employee", user.getUserId());
//        IndexResponse response = builder.setSource(jsonBuilder().startObject().field("name", user.getName()).field("userSettings", user.getUserSettings()).endObject()).get();
//        
//       // 
//               System.out.println("response id:"+response.getId());
//        return response.getResult().toString();
//    }
    
    
    @GetMapping("/view/{id}")
    public Map<String, ProfileShardResult> view(@PathVariable final String id) {
      //  GetResponse getResponse = client.client().prepareGet("users", "employee", id).get();
    	
    	RestHighLevelClient client = restClient.client();
    	SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
    	
		if (MUST_MATCH.length > 0) {
			for (String match : MUST_MATCH) {
				qb.must(QueryBuilders.matchQuery(MATCH_FIELD, match));
			}
		}
		
		if (MUST_NOT_MATCH.length > 0) {
			for (String notMatch : MUST_NOT_MATCH) {
				qb.mustNot(QueryBuilders.matchQuery(MATCH_FIELD, notMatch));
			}
		}

		qb.must(QueryBuilders.rangeQuery(TIME_FIELD).gte(START_TIME));
		qb.must(QueryBuilders.rangeQuery(TIME_FIELD).lte(END_TIME));

		searchSourceBuilder.query(qb).fetchSource(FETCH_FIELDS, null);

		searchRequest.indices(INDEX);
		searchRequest.source(searchSourceBuilder);
		
    	SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	if (searchResponse.getHits().getTotalHits().value > 0) {

			System.out.println(searchResponse.getHits().getTotalHits());
			
			for (SearchHit hit : searchResponse.getHits()) {
				System.out.println("Match: ");
				for (String fetchField : FETCH_FIELDS) {
					System.out.println(" - " + fetchField + " " + hit.getSourceAsMap().get(fetchField));
				}
			}
		} else {
			System.out.println("No results matching the criteria.");
		}
    	
        return searchResponse.getProfileResults();
    }
    
    
//    @GetMapping("/view/name/{field}")
//    public Map<String, Object> searchByName(@PathVariable final String field) {
//        Map<String,Object> map = null;
//        SearchResponse response = client.prepareSearch("users")
//                                .setTypes("employee")
//                                .setSearchType(SearchType.QUERY_THEN_FETCH)
//                                .setQuery(QueryBuilders.matchQuery("name", field))
//                                .get()
//                                ;
//        SearchHits searchHits = response.getHits();
//        SearchHit[] searchHitArr = searchHits.getHits();
//        map =   searchHitArr[0].getSourceAsMap();
//        return map;
//    }
//    
//    
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable final String id) {
//        DeleteResponse deleteResponse = client.prepareDelete("users", "employee", id).get();
//        return deleteResponse.getResult().toString();
//        
//    }
    
    
    
    
}
