package ca.ualberta.papaya.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.exceptions.UserInvalidPostalException;
import ca.ualberta.papaya.fixtures.Country;
import ca.ualberta.papaya.fixtures.Province;
import ca.ualberta.papaya.interfaces.IObserver;
import io.searchbox.annotations.JestId;

/**
 * Created by martin on 10/02/16.
 *
 * Model Class representing a User that can own Things and bid on other Users' Things
 * @see ElasticModel
 */

public class User extends ElasticModel {
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

    private String firstName = "";
    private String lastName = "";
    private String email = "";

    private String address1 = "";
    private String address2 = "";
    private Province province = null;
    private Country country = null;
    private String postal = "";

    public User(){
        super();
        kind = User.class;
    }

    public String getFirstName(){ return firstName; }
    public User setFirstName(String firstName){
        this.firstName = firstName;
        changed();
        return this;
    }

    public String getLastName(){ return lastName; }
    public User setLastName(String lastName){
        this.lastName = lastName;
        changed();
        return this;
    }

    public String getFullName(){ return getFirstName() + " " + getLastName(); }
    public String getName(){ return getFullName(); } // in case we want to switch to first names

    public String getEmail(){ return email; }
    public User setEmail(String email){
        this.email = email;
        changed();
        return this;
    }

    public String getAddress1(){ return address1; }
    public User setAddress1(String address){
        this.address1 = address1;
        changed();
        return this;
    }

    public String getAddress2(){ return address2; }
    public User setAddress2(String address){
        this.address2 = address2;
        changed();
        return this;
    }

    public Province getProvince(){ return province; }
    public User setProvince(Province province) throws NullPointerException{
        if (province == null){
            throw new NullPointerException();
        }
        this.province = province;
        changed();
        return this;
    }

    public Country getCountry(){ return country; }
    public User setCountry(Country country) throws NullPointerException{
        if (country == null){
            throw new NullPointerException();
        }
        this.country = country;
        changed();
        return this;
    }

    public String getPostal(){ return postal; }
    public User setPostal(String postal) throws UserInvalidPostalException {
        // todo: verify
        this.postal = postal;
        changed();
        return this;
    }

    public void getBids(IObserver observer){
        try {
            JSONObject json = new JSONObject();
            json.put("from", 0);
            json.put("size", 1000);

            JSONObject queryJson = new JSONObject();
            json.put("query", queryJson);

            JSONObject termJson = new JSONObject();
            queryJson.put("term", termJson);

            termJson.put("bidderId", getId());

            Bid.search(observer, Bid.class, json.toString());

        } catch (JSONException e){
            e.printStackTrace();
        }


    }

    public void getThings(IObserver observer){
        try {
            JSONObject json = new JSONObject();
            json.put("from", 0);
            json.put("size", 1000);

            JSONObject queryJson = new JSONObject();
            json.put("query", queryJson);

            JSONObject termJson = new JSONObject();
            queryJson.put("term", termJson);

            termJson.put("ownerId", getId());

            Thing.search(observer, Thing.class, json.toString());

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String toString(){
        return getFullName();
    }

}

