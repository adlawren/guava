package ca.ualberta.papaya.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to help deal with the ElasticSearch communication.
 * Created by martin on 10/02/16.
 */
public abstract class ElasticModel {

    // Elastic search index.
    protected static final String index = "papaya";

    // Elastic search "type" of object. Override.
    protected static String type;

    // The java class representing the child objects. Override
    protected static Class<?> kind;

    // the id of this object.
    protected int id;

    /**
     * Depending on the the "type" and "kind" of the subcless,
     * try and find from local cache or ElasticSearch the object
     * with the given id
     * @param id the id to find.
     * @return the object with the given id. null on failure.
     */
    public static ElasticModel getById(int id){
        try {
            ElasticModel model = (ElasticModel) kind.newInstance();
            return model;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Return the integer id of this object.
     * @return this object's unique id. Null if not yet committed.
     */
    public int getId(){
        return id;
    }

    protected void changed(){
        if(!ElasticChangeSet.contains(this)) {
            ElasticChangeSet.add(this);
        }
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
