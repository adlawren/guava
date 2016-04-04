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

    private String username = "";
    private String password = "";

    private String firstName = "";
    private String lastName = "";

    private String email = "";
    private String phone = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String province = null;
    private String country = null;
    private String postal = "";

    public User(){
        super();
        kind = User.class;
    }

    public String getUsername(){ return username; }
    public User setUsername(String username){
        this.username = username;
        changed();
        return this;
    }

    // todo: hash
    public boolean checkPassword(String password){ return true; }
    public User setPassword(String password){
        this.password = password;
        changed();
        return this;
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

    public String getPhone(){ return phone; }
    public User setPhone(String phone){
        this.phone = phone;
        changed();
        return this;
    }

    public String getAddress1(){ return address1; }
    public User setAddress1(String address){
        this.address1 = address;
        changed();
        return this;
    }

    public String getAddress2(){ return address2; }
    public User setAddress2(String address){
        this.address2 = address;
        changed();
        return this;
    }

    public String getCity(){ return city; }
    public User setCity(String city){
        this.city = city;
        changed();
        return this;
    }

    public String getProvince(){ return province; }
    public User setProvince(String province) { //} throws NullPointerException{
        //if (province == null){
        //    throw new NullPointerException();
        //}
        this.province = province;
        changed();
        return this;
    }

    public String getCountry(){ return country; }
    public User setCountry(String country) { // throws NullPointerException{
        // if (country == null){
        //    throw new NullPointerException();
        // }
        this.country = country;
        changed();
        return this;
    }

    public String getPostal(){ return postal; }
    public User setPostal(String postal) { // throws UserInvalidPostalException {
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
            queryJson.put("match", termJson);

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

    public void onDelete() {
        // no op. no way to delete a user right now.
    }

}

