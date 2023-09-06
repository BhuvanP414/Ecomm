package tech.springboot.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.domain.ProductEntity;
import tech.springboot.ecommerce.data.dto.ProductBasicInfoDto;
import tech.springboot.ecommerce.service.CategoryService;
import tech.springboot.ecommerce.service.ProductService;
import tech.springboot.ecommerce.utils.CategoryTreeMaker;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/index.html")
    public String renderHomepage(Model model) {
        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");
        model.addAttribute("categories", categories);

        List<ProductEntity> products = Collections.emptyList();
        if (!rootCategories.isEmpty()) {
            Random random = new Random();
            products = productService.productsForCategory(rootCategories.get(random.nextInt(rootCategories.size())).getId())
                    .stream()
                    .limit(8)
                    .collect(Collectors.toList());
        }
        List<ProductBasicInfoDto> productsSimple = productService.getBasicInfo(products);
        model.addAttribute("products", productsSimple);

        return "index.html";
    }
}
