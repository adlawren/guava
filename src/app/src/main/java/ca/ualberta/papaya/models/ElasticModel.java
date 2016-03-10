package ca.ualberta.papaya.models;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.annotations.JestId;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Abstract class to help deal with the ElasticSearch communication.
 * Created by martin on 10/02/16.
 */
public abstract class ElasticModel implements Serializable {

    private static JestClient client = null;

    // Elastic Search url
    private static final String elasticUrl = "http://cmput301.softwareprocess.es:8080/";

    // Elastic search index.
    protected static final String index = "papaya";

    // Elastic search "type" of object. Override.
    protected static String type;

    // The java class representing the child objects. Override
    protected static Class<?> kind;

    // the id of this object.
    @JestId
    protected String id;

    // is this model supposed to be pushed to database?
    protected boolean publish = false;

    /**
     * Construct (if necessary) and return a jest client to
     * talk to the ElasticSearch database.
     * @return the jest client
     */
    protected static JestClient getClient(){
        if (ElasticModel.client == null){
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(new DroidClientConfig
                    .Builder(elasticUrl)
                    .multiThreaded(true)
                    .build());
            ElasticModel.client = factory.getObject();
        }
        return ElasticModel.client;
    }

    /**
     * Depending on the the "type" and "kind" of the subcless,
     * try and find from local cache or ElasticSearch the object
     * with the given id
     * @param id the id to find.
     * @return the object with the given id. null on failure.
     */
    public static ElasticModel getById(String id){
        Get get = new Get.Builder(ElasticModel.index, id).type(kind.getName()).build();
        try {
            JestResult result = ElasticModel.getClient().execute(get);
            ElasticModel model = (ElasticModel) result.getSourceAsObject(kind);
            return model;
        } catch (IOException e){
            // TODO: make more robust
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a list of model objects corresponding to the given search query
     * @param search
     * @return List of model objects
     */
    public static List<?> search(Search search){
        try {
            SearchResult result = ElasticModel.client.execute(search);
            return result.getHits(kind);
        } catch (IOException e){
            // todo: make more robust
            e.printStackTrace();
            return null;
        }
    }

    public static void delete(ElasticModel model){
        try {
            getClient().execute(new Delete.Builder(model.getId())
                    .index(ElasticModel.index)
                    .type(kind.getName())
                    .build());
        } catch (IOException e){
            // todo: make more robust
            e.printStackTrace();
        }
    }

    /**
     * Constructor.
     */
    public ElasticModel(){

    }

    /**
     * publish this model object to database.
     */
    public void publish(){
        publish = true;
        changed();
    }

    /**
     * Return the id of this object.
     * @return this object's unique id. Null if not yet committed.
     */
    public String getId(){
        return id;
    }

    protected void changed(){
        if (publish) {
            ElasticChangeSet.add(this);
        }
    }

    private static class ElasticChangeSet {
        private static List<ElasticModel> changeList = new ArrayList<ElasticModel>();

        public static Boolean contains(ElasticModel model){
            return changeList.contains(model);
        }

        public static void add(ElasticModel model){
            if (!changeList.contains(model)) {
                changeList.add(model);
            }
            commit();
        }

        public static void commit(){
            for (ElasticModel model : changeList){
                if (model.id == null){
                    //insert
                    // todo: offline storage
                    Index index = new Index.Builder(model)
                            .index(ElasticModel.index)
                            .type(model.kind.getName()).build();
                    try {
                        ElasticModel.getClient().execute(index);
                        changeList.remove(model);
                    } catch (IOException e){
                        // todo: make more robust
                        e.printStackTrace();
                    }
                } else {
                    // update
                    // OMG PANIC. :j No "updates" allowed. k?
                }
            }
        }

    }

}
