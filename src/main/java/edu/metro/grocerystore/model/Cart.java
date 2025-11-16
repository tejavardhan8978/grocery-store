package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
public class Cart {


    //Need to do this-> ? https://www.objectdb.com/java/jpa/entity/id https://stackoverflow.com/a/19813646 https://www.baeldung.com/spring-jpa-embedded-method-parameters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;


    // List<Product> cartItems = new ArrayList<Product>();
    //Things to note: lists can only be objects, List<int> won't work
    // BUT if you try List<Integer> the compiler complains about hibernate or JPA or something as not a valid data type,
    //to get around this we could type casting
    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItem = new ArrayList<>();;


    // Ref: https://www.geeksforgeeks.org/java/hibernate-primarykeyjoincolumn-annotation/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User users;

    @Column(name="created_at")
    Instant createdAt;


    public Cart(){
        this.cartItem = new ArrayList<CartItem>(0);
    }


    public List<CartItem> getCartItems() {return cartItem;}

    public void setCartItems(List<CartItem> cartItems) {this.cartItem = cartItems;}

    public boolean addProductById(Integer productId) {
        //add product to user cart sql coding here?


        return true;
    }

    //Iterate through user cart using the quantities and prices from Product class
    public Double getTotal() {
            Double total = 0.00;

        for (CartItem item : this.getCartItems()) {
            //TODO: Search for product id and multiply by the product price;
            total += item.getQuantityPurchased();
        }

        return total;
    }


}
