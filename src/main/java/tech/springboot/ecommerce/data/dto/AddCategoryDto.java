package tech.springboot.ecommerce.data.dto;

import java.util.UUID;

public class AddCategoryDto {
    private String name;

    private UUID parentCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(UUID parentCategory) {
        this.parentCategory = parentCategory;
    }
}
