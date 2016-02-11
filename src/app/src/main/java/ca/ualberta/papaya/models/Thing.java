package ca.ualberta.papaya.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;

/**
 * Created by martin on 10/02/16.
 */
public class Thing extends ElasticModel {

    protected static final String type = "thing";
    protected static final Class<?> kind = Thing.class;

    private int ownerId;
    private String ownerName;

    private int borrowerId;
    private String borrowerName;

    private String title = "";
    private Thing.Status status = Status.AVAILABLE;
    private ArrayList<Tag> tags = new ArrayList<Tag>();
    private String description = "";
    private Bitmap thumbnail; // todo: initialize with default blank photo.
    private Bitmap photo;     // todo: initialize with default blank photo.

    public Thing(User owner){

        super();
    }

    public User getOwner(){ return (User)User.getById(ownerId); }
    public String getOwnerName(){ return ownerName; }
    public Thing setOwner(User owner){
        ownerId = owner.getId();
        ownerName = owner.getName();
        changed();
        return this;
    }

    public User getBorrower(){ return (User)User.getById(borrowerId); }
    public String getBorrowerName(){ return borrowerName; }
    public Thing setBorrower(User borrower){
        borrowerId = borrower.getId();
        borrowerName = borrower.getName();
        return this;
    }

    public String getTitle(){ return title; }
    public Thing setTitle(String title){
        this.title = title;
        changed();
        return this;
    }

    public Status getStatus(){ return this.status; }

    public List<Tag> getTags(){ return new ArrayList<>(this.tags); }
    public Thing addTag(Tag tag){
        return this;
    }

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
            setBorrower(bid.getBidder());
            status = Status.BORROWED;
            return this;
        } else {
            throw new ThingUnavailableException();
        }
    }

    public List<Bid> getBids(){
        return new ArrayList<Bid>();
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



}
