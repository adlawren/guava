package ca.ualberta.papaya.models;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.exceptions.UserInvalidPostalException;
import ca.ualberta.papaya.fixtures.Country;
import ca.ualberta.papaya.fixtures.Province;

/**
 * Created by martin on 10/02/16.
 */

public class User extends ElasticModel {

    protected transient Class<?> kind;

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

    public List<Bid> getBids(){ return new ArrayList<Bid>(); }

    public List<Thing> getThings(){ return new ArrayList<Thing>(); }

    public String toString(){
        return getFullName();
    }

}

