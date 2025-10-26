package edu.metro.grocerystore;

import edu.metro.grocerystore.model.Cart;
import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class GroceryStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryStoreApplication.class, args);
        System.out.println("Hello World!");
        User testUser = new User();
        Cart testCart = testUser.getCart();
        List<Product> cartList = testCart.getCartItems();
        //cartList.add(); //Add a Product to the cart by (database) productid in products table

        //Things to note: lists can only be objects, List<int> won't work, but we get the following error trying to use List<Integer>
        //Error:
        //org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Association 'edu.metro.grocerystore.model.Cart.cartItems' targets the type 'java.lang.Integer' which is not an '@Entity' type


        //How do we store the items in the cart on the database side? comma separated value list associated with the userid??
    }

}
