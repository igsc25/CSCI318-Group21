package com.csci318.product.controller;

import com.csci318.product.controller.dto.ProductDTO;
import com.csci318.product.model.entity.Product;
import com.csci318.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Helpers ----------

    private ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductID(product.getProductID());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setBrand(product.getBrand());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategories(product.getCategories());

        return productDTO;
    }

    private Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        return Product.createProduct(
                productDTO.getProductName(),
                productDTO.getProductDescription(),
                productDTO.getBrand(),
                productDTO.getPrice(),
                productDTO.getAvailableQuantity(),
                productDTO.getCategory()
        );
    }

    // Logics ----------

    @PostMapping
    ProductDTO createProduct(@Valid @RequestBody Product product) {
        productService.createProduct(product);
        return toDTO(product);
    }

    @GetMapping("/{product-id}")
    ProductDTO findProductByID(@PathVariable Long productID) {
        Product product = productService.findProductByID(productID);
        return toDTO(product);
    }

    @GetMapping
    List<ProductDTO> findAllProducts() {
        List<Product> products = productService.findAllProducts();
        return products.stream().map(this::toDTO).toList();
    }

    @PutMapping("/{productID}")
    ProductDTO updateProduct(@PathVariable Long productID, @Valid @RequestBody ProductDTO newProduct) {
        Product product = productService.updateProductInformation(productID, toEntity(newProduct));
        return toDTO(product);
    }

    @DeleteMapping("/{productID}")
    void deleteProduct(@PathVariable Long productID) {
        productService.deleteProduct(productID);
    }
}


