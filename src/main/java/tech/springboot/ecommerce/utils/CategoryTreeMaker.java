package tech.springboot.ecommerce.utils;

import org.springframework.data.util.Pair;
import tech.springboot.ecommerce.data.domain.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryTreeMaker {
    private static final String PREFIX = "---";
    public static List<Pair<String, String>> buildTree(List<CategoryEntity> categories, String  prefix) {
        List<Pair<String, String>> categoriesPair = new ArrayList<>();
        for (CategoryEntity categoryEntity:categories) {
            Pair<String, String> category = Pair.of(categoryEntity.getId().toString(), prefix + categoryEntity.getName());
            categoriesPair.add(category);
            if (categoryEntity.getChildren() != null && !categoryEntity.getChildren().isEmpty()) {
                categoriesPair.addAll(buildTree(categoryEntity.getChildren(), prefix + PREFIX));
            }
        }
        return categoriesPair;
    }
}
