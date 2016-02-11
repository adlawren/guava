package ca.ualberta.papaya.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/02/16.
 */
public abstract class ElasticModel {

    protected final static String index = "papaya";

    protected static String type;
    protected static Class<?> kind;

    protected int id;

    public static ElasticModel getById(int id){
        try {
            ElasticModel model = (ElasticModel) kind.newInstance();
            return model;
        } catch (Exception e){
            return null;
        }
    }

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
