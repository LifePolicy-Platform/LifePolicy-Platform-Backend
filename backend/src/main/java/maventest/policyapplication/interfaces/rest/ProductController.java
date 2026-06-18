package maventest.policyapplication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.common.ReturnMsg;
import maventest.policyapplication.application.internal.queryservices.ProductQueryService;
import maventest.policyapplication.domain.entity.ProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "商品查詢")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductQueryService productQueryService;

    @GetMapping
    @Operation(summary = "查詢所有商品", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<List<ProductEntity>>> listAll() {
        return ResponseEntity.ok(ReturnMsg.success(productQueryService.findAll()));
    }

    @GetMapping("/{code}")
    @Operation(summary = "依商品代碼查詢", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<ProductEntity>> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(ReturnMsg.success(productQueryService.findByCode(code)));
    }
}