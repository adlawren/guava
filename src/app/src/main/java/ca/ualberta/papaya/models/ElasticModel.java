package ca.ualberta.papaya.models;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.annotations.JestId;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

/**
 * Abstract class to help deal with the ElasticSearch communication.
 * Created by martin on 10/02/16.
 */
public abstract class ElasticModel {

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
     * Return the integer id of this object.
     * @return this object's unique id. Null if not yet committed.
     */
    public String getId(){
        return id;
    }

    protected void changed(){
        if(!ElasticChangeSet.contains(this)) {
            ElasticChangeSet.add(this);
        }
        ElasticChangeSet.commit();
    }

    private static class ElasticChangeSet {
        private static List<ElasticModel> changeList = new ArrayList<>();

        public static Boolean contains(ElasticModel model){
            return changeList.contains(model);
        }

        public static void add(ElasticModel model){
            changeList.add(model);
        }

        public static void commit(){

        }
        public static void cancel(){

        }
    }

}
