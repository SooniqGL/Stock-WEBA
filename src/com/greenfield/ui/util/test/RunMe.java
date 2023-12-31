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

import java.util.List;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public class RunMe {
    private static final String NOTES_TYPE_NAME = "notes";
    private static final String DIARY_INDEX_NAME = "diary";

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
                RunMe.deleteTestIndex(jestClient);
                RunMe.createTestIndex(jestClient);
                RunMe.indexSomeData(jestClient);
                RunMe.readAllData(jestClient);
            } finally {
                // shutdown client
                jestClient.shutdownClient();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void createTestIndex(final JestClient jestClient) throws Exception {
        // create new index (if u have this in elasticsearch.yml and prefer
        // those defaults, then leave this out
        ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 0);
        jestClient.execute(new CreateIndex.Builder(DIARY_INDEX_NAME).settings(settings.internalMap())
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
        searchSourceBuilder.query(QueryBuilders.termQuery("note", "see"));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(DIARY_INDEX_NAME)
                .addType(NOTES_TYPE_NAME).build();
        
        System.out.println(searchSourceBuilder.toString());
        
        JestResult result = jestClient.execute(search);
        List<Note> notes = result.getSourceAsObjectList(Note.class);
        for (Note note : notes) {
            System.out.println(note);
        }
    }

    private static void deleteTestIndex(final JestClient jestClient) throws Exception {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(DIARY_INDEX_NAME).build();
        jestClient.execute(deleteIndex);
    }

    private static void indexSomeData(final JestClient jestClient) throws Exception {
        // Blocking index
        final Note note1 = new Note("mthomas", "Note1: do u see this - " + System.currentTimeMillis());
        
        Index index = new Index.Builder(note1).index(DIARY_INDEX_NAME).type(NOTES_TYPE_NAME).build();
        jestClient.execute(index);
        
        // assign the doc ID
        // if do not assign, the ID using the JestID field;
        // if JestID field is null, will auto assigned from Elastic Eng
        index = new Index.Builder(note1).index(DIARY_INDEX_NAME).type(NOTES_TYPE_NAME).id("a_b-c_yes").build();
        jestClient.execute(index);

        
        // Asynch index
        final Note note2 = new Note("mthomas", "Note2: do u see this - " + System.currentTimeMillis());
        index = new Index.Builder(note2).index(DIARY_INDEX_NAME).type(NOTES_TYPE_NAME).build();
        jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
            public void failed(Exception ex) {
            }

            public void completed(JestResult result) {
                note2.setId((String) result.getValue("_id"));
                System.out.println("completed==>>" + note2);
            }
        });

        // bulk index
        final Note note3 = new Note("mthomas", "Note3: do u see this - " + System.currentTimeMillis());
        final Note note4 = new Note("mthomas", "Note4: do u see this - " + System.currentTimeMillis());
        Bulk bulk = new Bulk.Builder()
                .addAction(new Index.Builder(note3).index(DIARY_INDEX_NAME).type(NOTES_TYPE_NAME).build())
                .addAction(new Index.Builder(note4).index(DIARY_INDEX_NAME).type(NOTES_TYPE_NAME).build())
                .build();
        JestResult result = jestClient.execute(bulk);

        Thread.sleep(2000);

        System.out.println(result.toString());
    }
}
