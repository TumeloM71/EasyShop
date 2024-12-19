package org.yearup.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Order {

    private int orderId;              // The order_id (Primary Key)
    private int userId;               // The user_id (Foreign Key to 'users' table)
    private Date date;                // The date when the order was placed
    private String address;           // Shipping address
    private String city;              // Shipping city
    private String state;             // Shipping state
    private String zip;               // Shipping zip code
    private BigDecimal shippingAmount; // Shipping amount (BigDecimal for accurate financial calculations)
    private ShoppingCart cart;
    private List<ShoppingCartItem> lineItems;

    // Default constructor
    public Order() {}

    public Order(int orderId, int userId, Date date, String address, String city, String state, String zip, BigDecimal shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public List<ShoppingCartItem> getLineItems() {
        return this.cart.getItems().values().stream().toList();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", shippingAmount=" + shippingAmount +
                '}';
    }
}
