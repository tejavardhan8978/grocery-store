package edu.metro.grocerystore.model;

import jakarta.persistence.*;

@Entity
@Table(name="addresses")
public class Address {
    //https://docs.hibernate.org/orm/6.6/introduction/html_single/#primary-key-column-mappings
    //For letting me know what I could use for postgre auto increment in generated value  https://www.geeksforgeeks.org/advance-java/hibernate-generatedvalue-annotation-in-jpa/


    @Id
    @Column(name="addressid")
    private Integer addressid;

    @Column(name="street")
    private String street;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="zip")
    private String zip;

    @Column(name="country")
    private String country;

    @Column(name="userid")
    private Integer userid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false,insertable=false, updatable=false)
    @MapsId
    private User appusers;

    public Address() {

    }

    public Address(int userid, String street, String city, String state, String zip, String country) {
        this.userid = userid;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public Integer getId() {
        return userid;
    }

    public void setId(int userid) {
        this.userid = userid;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
