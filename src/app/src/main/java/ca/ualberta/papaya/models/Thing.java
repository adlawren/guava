package ca.ualberta.papaya.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;
import io.searchbox.annotations.JestId;

/**
 * Created by martin on 10/02/16.
 *
 * Model class representing a thing that is owned by a user.
 * @see ElasticModel
 */
public class Thing extends ElasticModel {

    public Thing getThing() {
        return this;
    }

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

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String newId) {
        borrowerId = newId;
    }

    private String title = "";

    private Thing.Status status = Status.AVAILABLE;

    //private ArrayList<Tag> tags = new ArrayList<Tag>();

    private String description = "";


    private transient LatLng location = null;

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

    private Photo photo;


    public Thing(User owner){
        super();
        kind = Thing.class;
        photo = new Photo();

        ownerId = owner.getId();
    }

    public Thing(Thing otherThing) {
        super();
        id = otherThing.id;
        kind = otherThing.kind;

        ownerId = otherThing.ownerId;
        borrowerId = otherThing.borrowerId;

        status = otherThing.status;

        title = otherThing.title;
        description = otherThing.description;

        photo = otherThing.photo;
    }

    public void copyThing(Thing otherThing) {
        id = otherThing.id;
        kind = otherThing.kind;

        ownerId = otherThing.ownerId;
        borrowerId = otherThing.borrowerId;

        status = otherThing.status;

        title = otherThing.title;
        description = otherThing.description;

        photo = otherThing.photo;
    }

    public void getOwner(IObserver observer){
        User.getById(observer, User.class, ownerId);
        // TODO: memoize
	}

    public String viewOwner(){
        return ownerId;
    }

    public Thing setOwner(User owner){
        this.ownerId = owner.getId();
        this.owner = owner;
        changed();
        return this;
    }

    public void getBorrower(IObserver observer){
        User.getById(observer, User.class, borrowerId);
        // TODO: memoize
    }
    public Thing setBorrower(User borrower){
        this.borrowerId = borrower.getId();
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

    public void setStatus(Status newStatus) {
        status = newStatus;
    }

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
        final Thing toUpdate = this;

        if (status == Status.AVAILABLE) {
            bid.getBidder(new Observer<User>() {
                @Override
                public void update(User bidder) {

                    System.out.println("[Thing] Bidder: id: " + bidder.getId() + ", name: " +
                            bidder.getName());

                    setBorrower(bidder);
                    status = Status.BORROWED;

                    // Needed?
                    // toUpdate.setBorrower(bidder);
                    toUpdate.borrowerId = bidder.getId();
                    toUpdate.status = Status.BORROWED;

                    resetLastModified();

                    // changed();
                    // publish(); // test

//                    Observable<ArrayList<Thing>> observable = new Observable<>();
//                    observable.addObserver(new IObserver<ArrayList<Thing>>() {
//                        @Override
//                        public void update(ArrayList<Thing> data) {
//                            System.out.println("[Thing] Updated thing:");
//                            for (Thing updated :data) {
//                                System.out.println("Thing: id: " + updated.getId() + ", uuid: " +
//                                        updated.getUuid() + ", title: " + updated.getTitle() +
//                                        ", description: " + updated.getDescription() +
//                                        ", status: " + updated.getStatus());
//
//                                // ASSUMPTION: There is at least one item
//                                // status = updated.getStatus();
//                                // ...
//                            }
//                        }
//                    });
//
//                    ThrowawayElasticSearchController.UpdateThingTask updateThingTask =
//                            new ThrowawayElasticSearchController.UpdateThingTask(observable);
//                    updateThingTask.execute(getThing());

                    System.err.println("toUpdate status: " + toUpdate.status);

                    Observable<Thing> observable = new Observable<>();
                    observable.setData(toUpdate);
                    observable.addObserver(new IObserver<Thing>() {
                        @Override
                        public void update(Thing data) {
                            System.err.println("data status: " + data.status);
                        }
                    });

                    MyThingsDataManager.getInstance().update(observable);
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
            queryJson.put("match", termJson);

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


    public LatLng getLocation(){return location;}
    public void setLocation( LatLng location){this.location = location;}


    public void onDelete(){
        getBids(new Observer<List<Bid>>() {
            @Override
            public void update(List<Bid> bids) {
                for (final Bid bid : bids){
                    Bid.delete(new Observer<Bid>() {
                        @Override
                        public void update(Bid deletedBid) {
                            System.err.println("Deleted bid associated to thing: " + bid.getId());
                        }
                    }, Bid.class, bid);
                }
            }
        });
    }


}
