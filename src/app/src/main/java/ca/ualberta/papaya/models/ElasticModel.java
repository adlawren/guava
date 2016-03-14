package ca.ualberta.papaya.models;

import android.content.Context;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.util.Ctx;
import io.searchbox.annotations.JestId;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.sort.Sort;

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

    // The java class representing the child objects. Implement in subclasses
    protected Class<?> kind;

    // the id of this object.
    @JestId
    protected String id;


    // is this model supposed to be pushed to database?
    protected transient boolean publish = false;

    // has this model been pushed to database?
    protected transient boolean published = false;


    /**
     * return lowercase class name
     * @return type
     */
    private static String typeName(Class<?> kind){
        return kind.getSimpleName().toLowerCase();
    }
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
     * @param kind the class of the model to load
     * @param id the id to find.
     * @return the object with the given id. null on failure.
     */
    public static Object getById(Class<?> kind, String id){
        try {
            Get get = new Get.Builder(ElasticModel.index, id).type(typeName(kind)).build();
            return getClient().execute(get).getSourceAsObject(kind);
        } catch (IOException e){
            // TODO: make more robust
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a list of model objects corresponding to the given search query
     * @param kind  the class of the model(s) to load
     * @param query
     * @return List of model objects
     */
    public static List<?> search(Class<?> kind, String query, Sort sort){
        try {
            Search search = new Search.Builder(query).addIndex(index).addType(typeName(kind)).build();
            return getClient().execute(search).getSourceAsObjectList(kind);
        } catch (IOException e){
            // todo: make more robust
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void delete(Class<?> kind, ElasticModel model){
        try {
            
            getClient().execute(new Delete.Builder(model.getId())
                    .index(index)
                    .type(typeName(kind))
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

        private static String changeListFile = "change_list.sav";


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
                if (model.id == null && model.publish){
                    //insert
                    // todo: offline storage
                    model.published = true;
                    Index index = new Index.Builder(model)
                            .index(ElasticModel.index)
                            .type(typeName(model.kind)).build();
                    try {
                        ElasticModel.getClient().execute(index);
                        changeList.remove(model);
                    } catch (IOException e){
                        // todo: make more robust
                        model.published = false;
                        e.printStackTrace();
                    }
                } else {
                    // update
                    // OMG PANIC. :j No "updates" allowed. k?
                }
            }
        }

        public static void saveChangeListFile(){

            return;

            // todo: offline
            /*
            Gson gson = new Gson();
            String s = gson.toJson(changeList);

            FileOutputStream outputStream;

            try {
                outputStream = Ctx.get().openFileOutput(changeListFile, Context.MODE_PRIVATE);
                outputStream.write(s.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }

        public static void loadChangeListFile(){
            return;

            // todo: offline
        }

    }

}
