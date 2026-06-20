package maventest.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.service.ProductService;

@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductQueryController {
    
     private final ProductService productService;

    @GetMapping("/active")
    public ApiResponse<List<ProductEntity>> listActiveProducts() {
        return ApiResponse.ok(productService.listActiveProducts());
    }
}
