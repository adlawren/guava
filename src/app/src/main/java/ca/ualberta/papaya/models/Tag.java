package ca.ualberta.papaya.models;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.annotations.JestId;

/**
 * Created by martin on 10/02/16.
 *
 * Model class representing a Tag that will correspond to Thing
 * @see ElasticModel
 */
public class Tag extends ElasticModel {
    @JestId
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    protected transient Class<?> kind;

    private String label = "";
    private Tag.Status status = Status.NEW;

    public static List<Tag> parseString(String tagText){
        return new ArrayList<Tag>();
    }

    public static String stringify(List<Tag> tags){
        return "";
    }

    public Tag(){
        super();
        kind = Tag.class;
    }

    public String getLabel(){ return label; }
    public Tag setLabel(String label){
        this.label = label;
        changed();
        return this;
    }

    public String toString(){
        return label;
    }

    public enum Status {
        NEW, VERIFIED;
        @Override public String toString() {
            return name().toLowerCase();
        }
    }

    public void onDelete(){
        // no op (for now, will need to remove references from things if implemented)
    }

}
