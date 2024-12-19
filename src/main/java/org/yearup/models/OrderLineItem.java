package org.yearup.models;

public class OrderLineItem {

    private ShoppingCartItem lineItem;
    private int orderId;

    public OrderLineItem(int orderId, ShoppingCartItem lineItem) {
        this.orderId = orderId;
        this.lineItem = lineItem;
    }

    public ShoppingCartItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(ShoppingCartItem lineItem) {
        this.lineItem = lineItem;
    }

    public int getOrderId() {
        return orderId;
    }

}
