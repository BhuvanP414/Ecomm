package tech.springboot.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.springboot.ecommerce.data.dao.CategoryDao;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.dto.AddCategoryDto;
import tech.springboot.ecommerce.data.filter.TopLevelCategoryFilter;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryDao categoryDao;

    public List<CategoryEntity> findTopLevelCategories() {
        TopLevelCategoryFilter filter = new TopLevelCategoryFilter();
        return categoryDao.get(filter);
    }

    public CategoryEntity find(UUID categoryId) {
        return categoryDao.find(categoryId);
    }

    public CategoryEntity addNewCategory(AddCategoryDto addCategoryDto) {
        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setName(addCategoryDto.getName());
        if (addCategoryDto.getParentCategory() != null) {
            CategoryEntity parent = categoryDao.find(addCategoryDto.getParentCategory());
            newCategory.setParent(parent);
        }

        return categoryDao.insert(newCategory);
    }
}
