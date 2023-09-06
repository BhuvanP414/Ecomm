package tech.springboot.ecommerce.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.springboot.ecommerce.data.domain.CategoryEntity;
import tech.springboot.ecommerce.data.dto.AddProductDto;
import tech.springboot.ecommerce.service.CategoryService;
import tech.springboot.ecommerce.service.ProductService;
import tech.springboot.ecommerce.utils.CategoryTreeMaker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/admin/addProduct.html")
    public String renderAddProduct(Model model) {
        List<CategoryEntity> rootCategories = categoryService.findTopLevelCategories();
        List<Pair<String, String>> categories = CategoryTreeMaker.buildTree(rootCategories, "");
        model.addAttribute("productDto", new AddProductDto());
        model.addAttribute("allCategories", categories);

        return "addProduct.html";
    }

    @Transactional
    @PostMapping("/admin/addProduct.html")
    public String doAddProduct(@ModelAttribute("productDto") AddProductDto addProductDto, Model model, @RequestParam("image") MultipartFile file) throws IOException, URISyntaxException {
        // Do any validation you want here
        productService.insertNewProduct(addProductDto, file);

        return "redirect:addProduct.html";
    }

    @GetMapping("/product/images/{id}.png")
    public @ResponseBody byte[] getImage(@PathVariable(name = "id")UUID productId) throws IOException, URISyntaxException {
        URL filePath = ResourceUtils.getURL("uploads/images/" + productId + ".png");
        return Files.readAllBytes(Paths.get(filePath.toURI()));
    }
}
