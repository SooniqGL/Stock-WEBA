package com.greenfield.ui.handler.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.elasticsearch.common.settings.ImmutableSettings;

import com.greenfield.common.base.AppContext;
import com.greenfield.common.tool.FreeMarkerTool;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.search.facet.TermsFacet;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;

public class ElasticSearchMgr {
	// the index/type = store/message
	public static final String MESSAGESTORE_INDEX 	= "msgstore";
    public static final String MESSAGE_TYPE 		= "msg";
    
    private JestClient jestClient = null;
	private static JestClientFactory factory = null;
	
	
	private void init() {
		// Get Jest client - need to get URL list from properties file
		// get jest client only once in instance level
		if (jestClient != null) {
			return;
		}
		
		// get the factory in class level, only once;
		// need to see if it works fine with multiple threads
		if (factory == null) {
			
	        //HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://localhost:9200").multiThreaded(true).build();
			List<String> urlList = AppContext.getElasticUrlList();
	        HttpClientConfig clientConfig = new HttpClientConfig.Builder(urlList).multiThreaded(true).build();
	        factory = new JestClientFactory();
	        factory.setHttpClientConfig(clientConfig);
		}
		
        jestClient = factory.getObject();

	}
	
	public void closeClient() {
		try {
			if (jestClient != null) {
				jestClient.shutdownClient();
			}
		} catch (Exception e) {
			// ignore
			// debug
			e.printStackTrace();
		}
	}

	/** most time, do not use this, use the Curl commend to create the index/type */
	public void createMessageStoreIndex() throws Exception {
		init();
		
        // create new index (if u have this in elasticsearch.yml and prefer
        // those defaults, then leave this out
        ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 0);
        jestClient.execute(new CreateIndex.Builder(MESSAGESTORE_INDEX).settings(settings.internalMap())
                .build());
    }
	
	// without error handling, simple indexing.
	public void indexMessage(MessageObject msg) throws Exception {
		init();
		
        // Blocking index
    	if (msg == null || msg.getId() == null) {
    		throw new Exception("ElasticSearchMgr/indexMessage: msg or msg id is null.");
    	}
        
        // we do not use JestId - but use external Id
        Index index = new Index.Builder(msg).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).id(msg.getId()).build();
        
        jestClient.execute(index);
        
	}
	
	// with error handling, better used for debug.
	public void indexMessageAsync(MessageObject msg) throws Exception {
		init();
		
        // Blocking index
    	if (msg == null || msg.getId() == null) {
    		throw new Exception("ElasticSearchMgr/indexMessage: msg or msg id is null.");
    	}
        
        // we do not use JestId - but use external Id
        Index index = new Index.Builder(msg).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).id(msg.getId()).build();
        
        jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
            public void failed(Exception ex) {
            	ex.printStackTrace();
            }

            // explain the error!!!
            public void completed(JestResult result) {
            	// debug
            	String error = result.getErrorMessage();
            	if (error != null) {
            		System.out.println("error =>" + error +", ID: " + result.getValue("_id"));
            	}
            }
        });
        
        // needed?
        Thread.sleep(2000);
	}
	
	// bulk call to index a list of message's 
	public void indexMessageBulk(List<MessageObject> msgList) throws Exception {
		init();
		
		Bulk.Builder bulk = new Bulk.Builder();
		
		if (msgList != null) {
			for (int i = 0; i < msgList.size(); i ++) {
				bulk.addAction(new Index.Builder(msgList.get(i)).index(MESSAGESTORE_INDEX).type(MESSAGE_TYPE).build());
			}
	
			jestClient.execute(bulk.build());
		}
		
		// needed ???
		Thread.sleep(2000);
	}
	
	public void deleteMessageStoreIndex() throws Exception {
		init();
		
        DeleteIndex deleteIndex = new DeleteIndex.Builder(MESSAGESTORE_INDEX).build();
        jestClient.execute(deleteIndex);
    }
	
	/**
	 * Search without Focet, only one group a time.
	 * This is called when the group id is given.  Usually after the first one with all groups search is done and
	 * users like to click the group level search.
	 * @param templateKey
	 * @param groups
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	public List<MessageObject> searchMessageForGivenGroup(String group, String keywords) throws Exception {
		init();
		
		if (group == null || group.equals("") || keywords == null || keywords.equals("")) {
			return null;
		}
		
        // remove the double quotes in input string
		group = group.replaceAll("\"", "");
        keywords = keywords.replaceAll("\"", "");
        
        HashMap<String, Object> input = new HashMap<String, Object>();
        input.put("group", group);
        input.put("keywords", keywords);
        
        String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_message_query.ftl", input);

        //String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_message2_query.ftl", input);
        Search search = new Search.Builder(queryStr).addIndex(MESSAGESTORE_INDEX)
                .addType(MESSAGE_TYPE).build();
        
        // debug
        System.out.println(queryStr);
        
        JestResult result = jestClient.execute(search);
        List<MessageObject> msgList = result.getSourceAsObjectList(MessageObject.class);
        
        // debug
        for (MessageObject note : msgList) {
            System.out.println(note);
        }
        
        String error = result.getErrorMessage();
        if (error != null) {
        	System.out.println("Error: " + error);
        	throw new Exception("ElasticSearchMgr: " + error);
        }
        
        return msgList;
    }
	
	// get suggestion for user typing
	public List<MessageObject> searchMessagePrefixWithGroups(List<String> groups, String keywords) throws Exception {
		init();
		
		if (groups == null || groups.size() == 0 || keywords == null || keywords.equals("")) {
			return null;
		}
		
        // remove the double quotes in input string
        keywords = keywords.replaceAll("\"", "");
        
        // construct the group string - "{group}",...
        StringBuilder groupStr = new StringBuilder();
        for (int i = 0; i < groups.size(); i ++) {
        	if (i > 0) {
        		groupStr.append(",");
        	}
        	groupStr.append("\"").append(groups.get(i).replaceAll("\"", "")).append("\"");
        }

        HashMap<String, Object> input = new HashMap<String, Object>();
        input.put("groups", groupStr);
        input.put("keywords", keywords);
        
        String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_prefix_query.ftl", input);
        
        //String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_message2_query.ftl", input);
        Search search = new Search.Builder(queryStr).addIndex(MESSAGESTORE_INDEX)
                .addType(MESSAGE_TYPE).build();
        
        // debug
        System.out.println(queryStr);
        
        JestResult result = jestClient.execute(search);
        List<MessageObject> msgList = result.getSourceAsObjectList(MessageObject.class);
        
        // debug
        for (MessageObject note : msgList) {
            System.out.println(note);
        }
        
        String error = result.getErrorMessage();
        if (error != null) {
        	System.out.println("Error: " + error);
        	throw new Exception("ElasticSearchMgr: " + error);
        }
 
        return msgList;
    }
	
	public FacetsResult searchMessageWithGroupFacets(List<String> groups, String keywords) throws Exception {
		init();
		
		if (groups == null || groups.size() == 0 || keywords == null || keywords.equals("")) {
			return null;
		}
		
        // remove the double quotes in input string
        keywords = keywords.replaceAll("\"", "");
        
        // construct the group string - "{group}",...
        StringBuilder groupStr = new StringBuilder();
        for (int i = 0; i < groups.size(); i ++) {
        	if (i > 0) {
        		groupStr.append(",");
        	}
        	groupStr.append("\"").append(groups.get(i).replaceAll("\"", "")).append("\"");
        }

        HashMap<String, Object> input = new HashMap<String, Object>();
        input.put("groups", groupStr);
        input.put("keywords", keywords);
        
        String queryStr = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_facets_query.ftl", input);
        Search search = new Search.Builder(queryStr).addIndex(MESSAGESTORE_INDEX)
                .addType(MESSAGE_TYPE).build();
        
        // debug
        System.out.println(queryStr);
        
        JestResult result = jestClient.execute(search);
        List<MessageObject> msgList = result.getSourceAsObjectList(MessageObject.class);
        
        // debug
        for (MessageObject note : msgList) {
            System.out.println(note);
        }
        
        String error = result.getErrorMessage();
        if (error != null) {
        	System.out.println("Error: " + error);
        	throw new Exception("ElasticSearchMgr: " + error);
        }
        
        FacetsResult facets = new FacetsResult();
        List<TermsFacet> termsFacets = result.getFacets(TermsFacet.class);
        
        ArrayList<GroupCount> cntList = new ArrayList<GroupCount>();
        // we only to one set of facets now, so the next loop only goes once at most
        for (int i = 0; i < termsFacets.size(); i ++) {
        	TermsFacet f = termsFacets.get(i);
        	List<TermsFacet.Term> ff = f.terms();
        	
        	// debug
        	System.out.println(" facets: " + termsFacets.get(i).getName() + ", total: " + termsFacets.get(i).getTotal());
        	
        	
        	for (int j = 0; j < ff.size(); j ++) {
        		TermsFacet.Term term = ff.get(j);
        		GroupCount cnt = new GroupCount();
        		cnt.setGroupId(term.getName());
        		cnt.setCount(term.getCount());
        		cnt.setGroupName("");   // Get Group Name from session ???
        		cntList.add(cnt);
        		
        		// debug
        		System.out.println("name: " + ff.get(j).getName() + ", cnt: " + ff.get(j).getCount());
        	}
        }

        facets.setMessageList(msgList);
        facets.setTermList(cntList);
        
        return facets;
    }
}
