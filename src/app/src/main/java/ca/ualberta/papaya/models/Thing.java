package ca.ualberta.papaya.models;

import android.graphics.Bitmap;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;

/**
 *
 * Created by martin on 10/02/16.
 */
public class Thing extends ElasticModel {

    protected transient Class<?> kind;

    private String ownerId;
    private String ownerName;

    // TODO: Fix
    private User owner;

    private String borrowerId;
    private String borrowerName;

    private String title = "";
    private Thing.Status status = Status.AVAILABLE;
    private ArrayList<Tag> tags = new ArrayList<Tag>();
    private String description = "";

    private Photo photo = new Photo();     // todo: initialize with default blank photo.

    public static List<Thing> getMyThings(){
        return new ArrayList<Thing>();
    }

    public static List<Thing> getThings(){
        return (List<Thing>) Thing.search(Thing.class, "{}", null);
    }

    public static List<Thing> getThings(List<String> keywords){
        return new ArrayList<Thing>();
    }


    public Thing(User owner){
        super();
        kind = Thing.class;

		// TODO: Update
        this.owner = owner;
        ownerName = owner.getName();
        id = UUID.randomUUID().toString();
    }

    public User getOwner(){ 
		//return (User)User.getById(kind, ownerId);
		
		// TODO: Fix
        return owner;
	}
    public String getOwnerName(){ 
		//return ownerName;
		
		// TODO: Fix
        return owner.getName();
	}
    public Thing setOwner(User owner){
        ownerId = owner.getId();
        ownerName = owner.getName();
        changed();
        return this;
    }

    public User getBorrower(){ return (User)User.getById(kind, borrowerId); }
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
