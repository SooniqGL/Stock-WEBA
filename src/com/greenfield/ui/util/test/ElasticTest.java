package com.greenfield.ui.util.test;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.greenfield.common.tool.FreeMarkerTool;
import com.greenfield.ui.handler.group.MessageObject;


public class ElasticTest {
	// the index/type = store/message
	public static final String MESSAGESTORE_INDEX 	= "msgstore";
    public static final String MESSAGE_TYPE 	= "msg";
    

    public static void main(String[] args) {
        try {
            // Get Jest client
            HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://localhost:9200").multiThreaded(true)
                    .build();
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(clientConfig);
            JestClient jestClient = factory.getObject();

            try {
                // run test index & searching
            	//ElasticTest.deleteMessageStoreIndex(jestClient);
            	//ElasticTest.createMessageStoreIndex(jestClient);
            	// ElasticTest.indexSomeData(jestClient);
            	//ElasticTest.readAllData(jestClient);
            	
            	
            	ElasticTest.readAllData2(jestClient, "45221", "rabbit jump mes");
            } finally {
                // shutdown client
                jestClient.shutdownClient();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void createMessageStoreIndex(final JestClient jestClient) throws Exception {
        // create new index (if u have this in elasticsearch.yml and prefer
        // those defaults, then leave this out
        ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 0);
        jestClient.execute(new CreateIndex.Builder(MESSAGESTORE_INDEX).settings(settings.internalMap())
                .build());
    }

    /*
    QueryBuilder queryBuilder = QueryBuilders.termQuery("brief", "jazz");
    Search search = new Search(queryBuilder);
    search.addIndex("music_reviews");
    search.addType("review");
    JestResult result = client.execute(search);

    List<MusicReview> reviewList = result.getSourceAsObjectList(MusicReview.class);
    for(MusicReview review: reviewList){
      System.out.println("search result is " + review);
    } */
    
    private static void readAllData(final JestClient jestClient) throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("g", "P"));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(MESSAGESTORE_INDEX)
                .addType(MESSAGE_TYPE).build();
        
        System.out.println(searchSourceBuilder.toString());
        
        JestResult result = jestClient.execute(search);
        List<MessageObject> msgList = result.getSourceAsObjectList(MessageObject.class);
        for (MessageObject note : msgList) {
            System.out.println(note);
        }
    }
    
    /**
     * group given, and keyword list given
     * and search it:
     * @param jestClient
     * @throws Exception
     */
    private static void readAllData2(final JestClient jestClient, String group, String keywords) throws Exception {
        group = group.replaceAll("\"", "");
        keywords = keywords.replaceAll("\"", "");

        HashMap<String, Object> input = new HashMap<String, Object>();
        input.put("groups", group);
        input.put("keywords", keywords);
        
        String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_prefix_query.ftl", input);
        //String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_message2_query.ftl", input);
        Search search = new Search.Builder(queryStr).addIndex(MESSAGESTORE_INDEX)
                .addType(MESSAGE_TYPE).build();
        
        System.out.println(queryStr);
        
        JestResult result = jestClient.execute(search);
        List<MessageObject> msgList = result.getSourceAsObjectList(MessageObject.class);
        for (MessageObject note : msgList) {
            System.out.println(note);
        }
        
        System.out.println("Error: " + result.getErrorMessage());
    }

    private static void deleteMessageStoreIndex(final JestClient jestClient) throws Exception {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(MESSAGESTORE_INDEX).build();
        jestClient.execute(deleteIndex);
    }

    /*
     * public MessageObject(String id, String subject, String content, 
    		Date messageDate, String userName, 
    		String userId, ArrayList<String> groupList) {
     */
    private static void indexSomeData(final JestClient jestClient) throws Exception {
        // Blocking index
    	ArrayList<String> groupList = 
    			new ArrayList<String>(
    			Arrays.asList(
    					"P", "45221", "G36"  
    					));
    	
        MessageObject msg = new MessageObject("abcd-133", "Subject 1: do u message jumping rabbits  aeae see this - " + System.currentTimeMillis(), "Content message see # 333 and good",
        		new Date(), "user one", "U20", groupList);
        
        // we do not use JestId - but use external Id
        Index index = new Index.Builder(msg).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).id(msg.getId()).build();
        
        jestClient.execute(index);
        
        groupList = 
    			new ArrayList<String>(
    			Arrays.asList(
    					"PP", "45220", "G366"  
    					));
        
        // Asynch index
        final MessageObject msg2 = new MessageObject("abcd-134", "Subject 1: cccc do 6666 u see jump this - " + System.currentTimeMillis(), "Content see message # 444 and good",
        		new Date(), "user one", "U20", groupList);
        index = new Index.Builder(msg2).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).build();
        jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
            public void failed(Exception ex) {
            	ex.printStackTrace();
            }

            // explain the error!!!
            public void completed(JestResult result) {
                msg2.setId((String) result.getValue("_id"));
                System.out.println("error==>>" + result.getErrorMessage());
                System.out.println("completed==>>" + msg2);
            }
        });

        // bulk index
        groupList = 
    			new ArrayList<String>(
    			Arrays.asList(
    					"P", "45220", "G36"  
    					));
        final MessageObject msg3 = new MessageObject("abcd-135", "Subject 1: do aaa 444 u aa yes ggg see this - " + System.currentTimeMillis(), "Content see # yes 555 and good",
        		new Date(), "user one", "U20", groupList);
        
        groupList = 
    			new ArrayList<String>(
    			Arrays.asList(
    					"PP", "45040", "G366"  
    					));
        final MessageObject msg4 = new MessageObject("abcd-126", "Subject 1: do u 555 message bbbb see this - " + System.currentTimeMillis(), "Content see # 666 and good",
        		new Date(), "user one", "U20", groupList);
        Bulk bulk = new Bulk.Builder()
                .addAction(new Index.Builder(msg3).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).build())
                .addAction(new Index.Builder(msg4).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).build())
                .build();
        JestResult result = jestClient.execute(bulk);

        Thread.sleep(2000);

        System.out.println(result.toString());
    }
}
