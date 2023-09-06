package tech.springboot.ecommerce.data.dto;

public class AddToCartResponseDto {
    private int numItems;

    public AddToCartResponseDto(int numItems) {
        this.numItems = numItems;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int items) {
        this.numItems = items;
    }
}
