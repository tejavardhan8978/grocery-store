package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @Column(name="created_at")
    private Instant createdAt;

    //Ref: https://medium.com/@iam03rv/best-way-to-map-an-enum-type-with-jpa-and-hibernate-with-examples-9974c856f33d
    @Enumerated(EnumType.STRING)
    @Column(length=18)
    private OrderStatus orderstatus;


    public Order(){

    }

    public Order(User user, OrderStatus status){
        this.users = user;
        this.orderstatus = status;
        this.createdAt = Instant.now();
    }


}
