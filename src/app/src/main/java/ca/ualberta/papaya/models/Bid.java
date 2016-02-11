package ca.ualberta.papaya.models;

/**
 * Created by martin on 10/02/16.
 */
public class Bid extends ElasticModel {


    protected static final String type = "bid";
    protected static final Class<?> kind = Bid.class;

    private String bidderName;
    private int bidderId;
    private int amount; // in cents
    private Per per = Per.FLAT;
    private int thingId;



    public Bid(Thing thing, User bidder, int amount){
        super();
        setThing(thing);
        setBidder(bidder);
        setAmount(amount);
    }

    public User getBidder(){ return (User) User.getById(bidderId); }
    public Bid setBidder(User bidder){
        this.bidderId = bidder.getId();
        this.bidderName = bidder.getFullName();
        changed();
        return this;
    }

    public Thing getThing(){ return (Thing) Thing.getById(thingId); }
    public Bid setThing(Thing thing){
        thingId = thing.getId();
        changed();
        return this;
    }

    public int getAmount(){ return amount; }
    public Bid setAmount(int amount){
        this.amount = amount;
        changed();
        return this;
    }

    public Per getPer(){ return per; }
    public Bid setPer(Per per){
        this.per = per;
        changed();
        return this;
    }


    public String valueOf(){
        return (amount/100) + "." + (amount%100);
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
