package ca.ualberta.papaya.models;

import java.io.InvalidClassException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.exceptions.BidNegativeException;
import ca.ualberta.papaya.exceptions.InvalidLocationException;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.interfaces.IObserver;
import io.searchbox.annotations.JestId;

/**
 * Model class representing a Bid on a Thing owned by a User
 * @see ElasticModel
 *
 * Created by martin on 10/02/16.
 */
public class Bid extends ElasticModel {
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

    private String bidderName;       // Denormalized User.getName()
    private String bidderId;         // the bidder's .getId()
    private int amount;              // Amount of the bid (in cents)
    private Per per = Per.FLAT;      // Choice of rate plan. (flat rate, hourly, etc)
    private String thingId;          // the thing being bid on's .getId()
    private List<Number> location;   // Location [Longitude, Latitude]


    /**
     * Construct a new Bid on an item
     * @param thing the Thing being bid on
     * @param bidder the User performing making the Bid.
     * @param amount how much is being bid. (in cents)
     */
    public Bid(Thing thing, User bidder, int amount) throws ThingUnavailableException {
        super();
        kind = Bid.class;
        setAmount(amount);
        setThing(thing);
        setBidder(bidder);
    }

    /**
     * Construct a new Bid on an item at a time-based rate.
     * @param thing the Thing being bid on
     * @param bidder the User performing making the Bid.
     * @param amount how much is being bid. (in cents)
     * @param per the time per amount cost (hours, days, etc)
     */
    public Bid(Thing thing, User bidder, int amount, Per per) throws ThingUnavailableException {
        super();
        setThing(thing);
        setBidder(bidder);
        setAmount(amount);
        setPer(per);
    }

    public String getBidderName(){
        return this.bidderName;
    }
    public void getBidder(IObserver observer){
        User.getById(observer, User.class, bidderId);
    }
    public Bid setBidder(User bidder){
        this.bidderId = bidder.getId();
        this.bidderName = bidder.getFullName();
        changed();
        return this;
    }


    public String getBidderId() {
        return bidderId;
    }

    public void getThing(IObserver observer){
        Thing.getById(observer, Thing.class, thingId);
    }
    public Bid setThing(Thing thing) throws ThingUnavailableException {
        thingId = thing.getId();
        thing.placeBid(this);
        changed();
        return this;
    }

    public String getThingId() {
        return thingId;
    }

    public int getAmount(){ return amount; }
    public Bid setAmount(int amount) throws BidNegativeException {
        if(amount < 0){
            throw new BidNegativeException();
        }
        this.amount = amount;
        changed();
        return this;
    }

    public Per getPer(){ return per; }
    public Bid setPer(Per per) throws NullPointerException {
        if(per == null){
            throw new NullPointerException();
        }
        this.per = per;
        changed();
        return this;
    }

    public List getLocation(){ return location; }
    public Bid setLocation(List<Number> location){
        if (location.size() == 2){
            this.location = location;
        } else {
            throw new InvalidLocationException();
        }
        return this;
    }


    public String valueOf(){
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(((double)amount)/100);
    }

    public String toString(){
        return "$" + valueOf();
    }

    public enum Per {
        FLAT, HOUR, DAY, WEEK, MONTH;
        @Override public String toString() {
            return name().toLowerCase();
        }
    }

}
