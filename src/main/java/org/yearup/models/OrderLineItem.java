package org.yearup.models;

import java.math.BigDecimal;

public class OrderLineItem {
    private int orderLineItemId;
    private int productId;
    private BigDecimal salesPrice;
    private int quantity;
    private BigDecimal discount = BigDecimal.ZERO;

    public OrderLineItem() {
    }

    public OrderLineItem(int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public OrderLineItem(int orderLineItemId, int productId, int quantity, BigDecimal salesPrice, BigDecimal discount) {
        this.orderLineItemId = orderLineItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.salesPrice = salesPrice;
        this.discount = discount;
    }

    public int getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(int orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        if (discount == null){
            this.discount = BigDecimal.ZERO;
        }
        else {
            this.discount = discount;
        }

    }
}
