package org.yearup.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private LocalDateTime date;
    private String address;
    private String city;
    private String state;
    private String zip;
    private BigDecimal shippingAmount = BigDecimal.ZERO;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(int userId, LocalDateTime date, String address, String city, String state, String zip) {
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Order(int orderId, int userId, LocalDateTime date, String address, String city, String state, String zip) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    //Methods
    public Order addLineItem(OrderLineItem orderLineItem){
        this.orderLineItems.add(orderLineItem);
        return this;
    }

    public BigDecimal getTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(OrderLineItem orderLineItem: orderLineItems){
            BigDecimal line = orderLineItem.getSalesPrice()
                    .multiply(BigDecimal.valueOf(orderLineItem.getQuantity()));
            line = line.subtract(orderLineItem.getDiscount());
            total = total.add(line);
        }
        return total.add(shippingAmount);
    }
}
