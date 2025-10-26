package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart")
public class Cart {


    //Need to do this-> ? https://www.objectdb.com/java/jpa/entity/id https://stackoverflow.com/a/19813646 https://www.baeldung.com/spring-jpa-embedded-method-parameters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;



    @OneToMany(mappedBy = "cart")
    // List<Product> cartItems = new ArrayList<Product>();
    //Things to note: lists can only be objects, List<int> won't work
    // BUT if you try List<Integer> the compiler complains about hibernate or JPA or something as not a valid data type,
    //to get around this we could type casting
    List<Product> cartItems;


    // Ref: https://www.geeksforgeeks.org/java/hibernate-primarykeyjoincolumn-annotation/
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "cart_id", referencedColumnName = "userid")
    private User appusers;

    public Cart(){
        this.cartItems = new ArrayList<>(0);
    }



    public List<Product> getCartItems() {return cartItems;}


    public boolean addProductById(int productId) {
        //add product to user cart sql coding here?


        return true;
    }
}
