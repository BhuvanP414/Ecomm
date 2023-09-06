package tech.springboot.ecommerce.data.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemDto {
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private long numItems;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getNumItems() {
        return numItems;
    }

    public void setNumItems(long noItems) {
        this.numItems = noItems;
    }
}
