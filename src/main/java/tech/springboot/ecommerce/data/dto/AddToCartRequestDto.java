package tech.springboot.ecommerce.data.dto;

import java.util.UUID;

public class AddToCartRequestDto {
    private UUID productId;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
