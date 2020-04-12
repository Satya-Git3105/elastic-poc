package com.elastic.poc.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class Config {
	
	
	private static final String ElasticSearchConstants = null;
	//private TransportClient client = null;
    private Properties elasticPro = null;
	
	    @Value("${elasticsearch.host}") 
	    public String host;
	    @Value("${elasticsearch.port}") 
	    public int port;
	    
	    @Value("${elasticsearch.cluster}") 
	    public String cluster;
	    
	    //@Value("${elasticsearch.transport.sniff}") 
	   // public String sniff;
	    
	    
	    
	    public String getHost() {
	    		return host;
	    }
		public int getPort() {
				return port;
		}
		
		public String getCluster() {
			return cluster;
		}
		
	/*
	 * public String getSniff() { return sniff; }
	 */
		
		
	/*	 public Config() {
		        try {
		            elasticPro = new Properties();
		            elasticPro.load(ElasticsearchClient.class.getResourceAsStream(ElasticSearchConstants.ELASTIC_PROPERTIES));
		            log.info(elasticPro.getProperty("host"));
		        } catch (IOException ex) {
		            log.info("Exception occurred while load elastic properties : " + ex, ex);
		        }
		    }*/
		
		
		@Bean
		 public RestHighLevelClient client(){
			
			RestHighLevelClient client = null;
		       
		        try{
		            client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
		        	//client = new RestHighLevelClient(RestClient.builder(new HttpHost(getHost(), getPort(), "http")));
		            System.out.println("host:"+ host+"port:"+port);
		            
		            
		           // Config cfg = getTypeCfg();
		           // Settings settings = Settings.builder().put("cluster.name", getCluster()).build();
		          //  TransportClient client2 = new PreBuiltTransportClient(settings);
		            
		            
		            
		       
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		        return client;
		    }
		
		
	
//	 @Bean
//	 public Client client(){
//	        TransportClient client = null;
//	        try{
//	            System.out.println("host:"+ host+"port:"+port);
//	            
//	            
//	           // Config cfg = getTypeCfg();
//	            Settings settings = Settings.builder().put("cluster.name", getCluster()).build();
//	          //  TransportClient client2 = new PreBuiltTransportClient(settings);
//	            
//	            client = new PreBuiltTransportClient(settings);
//	            client.addTransportAddress(new TransportAddress(InetAddress.getByName(getHost()), getPort()));
//	            
//	        } catch (UnknownHostException e) {
//	            e.printStackTrace();
//	        } catch (Exception e) {
//	        	e.printStackTrace();
//	        }
//	        return client;
//	    }
}
