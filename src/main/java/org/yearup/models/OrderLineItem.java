package org.yearup.models;

import java.math.BigDecimal;

public class OrderLineItem {

    private ShoppingCartItem lineItem;
    private int orderId;
    private BigDecimal cost;

    public OrderLineItem(int orderId, ShoppingCartItem lineItem) {
        this.orderId = orderId;
        this.lineItem = lineItem;
        this.cost = lineItem.getLineTotal();
    }

    public BigDecimal getCost() {
        return cost;
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
