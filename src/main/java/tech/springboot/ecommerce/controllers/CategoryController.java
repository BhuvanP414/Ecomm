package tech.springboot.ecommerce.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.domain.ProductEntity;
import tech.springboot.ecommerce.data.dto.AddCategoryDto;
import tech.springboot.ecommerce.data.dto.ProductBasicInfoDto;
import tech.springboot.ecommerce.service.CategoryService;
import tech.springboot.ecommerce.service.ProductService;
import tech.springboot.ecommerce.utils.CategoryTreeMaker;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private static final int PRODUCTS_PER_PAGE = 2;

    private final CategoryService categoryService;

    private final ProductService productService;

    @GetMapping("/admin/addCategory.html")
    public String renderAddCategory(Model model) {
        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");

        model.addAttribute("categoryDto", new AddCategoryDto());
        model.addAttribute("existingCategories", categories);

        return "addCategory.html";
    }

    @Transactional
    @PostMapping("/admin/addCategory.html")
    public String doAddCategory(@ModelAttribute("categoryDto") AddCategoryDto addCategoryDto, Model model) {
        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");

        if (addCategoryDto.getName() == null || addCategoryDto.getName().isBlank()) {
            model.addAttribute("categoryDto", new AddCategoryDto());
            model.addAttribute("existingCategories", categories);
            model.addAttribute("error", "Name can't be empty");
            return "addCategory.html";
        }

        categoryService.addNewCategory(addCategoryDto);
        return "redirect:addCategory.html";
    }

    @GetMapping("/browse/{id}")
    public String browse(@PathVariable(name = "id") UUID categoryId, @RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");
        CategoryEntity category = categoryService.find(categoryId);

        List<ProductEntity> products = Collections.emptyList();
        boolean first = false;
        boolean last = true;

        if (page == 0) {
            first = true;
        }
        if (!rootCategories.isEmpty()) {
            products = productService.productsForCategory(categoryId, page * PRODUCTS_PER_PAGE, PRODUCTS_PER_PAGE);
            long totalProducts = productService.countProductsInCategory(categoryId);
            if (page * PRODUCTS_PER_PAGE + products.size() < totalProducts) {
                last = false;
            }
        }
        List<ProductBasicInfoDto> productsSimple = productService.getBasicInfo(products);

        model.addAttribute("first", first);
        model.addAttribute("last", last);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", productsSimple);

        return "category.html";
    }
}
