package ca.ualberta.papaya.models;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.util.Observer;
import io.searchbox.annotations.JestId;

/**
 * Created by martin on 10/02/16.
 *
 * Model class representing a thing that is owned by a user.
 * @see ElasticModel
 */
public class Thing extends ElasticModel {
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

    public transient Class<?> kind;

    private String ownerId;
    private transient User owner;

    private String borrowerId;
    private transient User borrower;

    private String title = "";

    private Thing.Status status = Status.AVAILABLE;

    //private ArrayList<Tag> tags = new ArrayList<Tag>();

    private String description = "";

    private Photo photo;
    private Location location;

    /*
    public static List<Thing> getMyThings(){
        return new ArrayList<Thing>();
    }

    public static List<Thing> getThings(){
        return (List<Thing>) Thing.search(Thing.class, "{}", null);
    }

    public static List<Thing> getThings(List<String> keywords){
        return new ArrayList<Thing>();
    }
    */

    public Thing(User owner){
        super();
        kind = Thing.class;
        photo = new Photo();

        ownerId = owner.getId();
        //id = UUID.randomUUID().toString();
    }

    public void getOwner(IObserver observer){
        User.getById(observer, User.class, ownerId);
        // TODO: memoize
	}

    public Thing setOwner(User owner){
        ownerId = owner.getId();
        this.owner = owner;
        changed();
        return this;
    }

    public void getBorrower(IObserver observer){
        User.getById(observer, kind, borrowerId);
        // TODO: memoize
    }
    public Thing setBorrower(User borrower){
        borrowerId = borrower.getId();
        this.borrower = borrower;
        changed();
        return this;
    }

    public String getTitle(){ return title; }
    public Thing setTitle(String title){
        this.title = title;
        changed();
        return this;
    }

    public Status getStatus(){ return this.status; }

    /*
    public List<Tag> getTags(){ return new ArrayList<>(this.tags); }
    public Thing addTag(Tag tag){

        return this;
    }
    */

    public String getDescription(){ return description; }
    public Thing setDescription(String description){
        this.description = description;
        changed();
        return this;
    }

    public Thing placeBid(Bid bid) throws ThingUnavailableException {
        if (status == Status.AVAILABLE){
            // ok!
            return this;
        } else {
            throw new ThingUnavailableException();
        }

    }

    public Thing acceptBid(Bid bid) throws ThingUnavailableException {
        if (status == Status.AVAILABLE) {
            bid.getBidder(new Observer<User>() {
                @Override
                public void update(User bidder) {
                    setBorrower(bidder);
                    status = Status.BORROWED;
                    changed();
                }
            });
            return this;
        } else {
            throw new ThingUnavailableException();
        }
    }

    public void getBids(Observer observer){
        try {
            JSONObject json = new JSONObject();
            json.put("from", 0);
            json.put("size", 1000);

            JSONObject queryJson = new JSONObject();
            json.put("query", queryJson);

            JSONObject termJson = new JSONObject();
            queryJson.put("term", termJson);

            termJson.put("thingId", getId());

            System.err.println("(getBids) " + json.toString());

            Bid.search(observer, Bid.class, json.toString());

        } catch (JSONException e){
            e.printStackTrace();
        }
    }



    public String toString(){
        return title;
    }

    public enum Status {
        AVAILABLE, UNAVAILABLE, BORROWED;
        @Override public String toString() {
            return name().toLowerCase();
        }
    }

    public Photo getPhoto(){ return photo;}
    public void setPhoto(Photo newPhoto){
        this.photo = newPhoto;
    }

    public Location getLocation(){return location;}
    public void setLocation( Location location){this.location = location;}

}
