package maventest.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiCode;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.product.service.ProductCommandService;
import maventest.product.service.ProductQueryService;
import maventest.product.entity.ProductEntity;
import maventest.product.dto.ProductCreateReqDto;
import maventest.product.dto.ProductUpdateReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "商品管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    @PostMapping
    @Operation(summary = "新增商品", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<ProductEntity>> create(
            @Valid @RequestBody ProductCreateReqDto dto,
            BindingResult bindingResult) {
        try {
            EntityUtil.validDto(bindingResult);
            ProductEntity created = productCommandService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReturnMsg.success(created));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    @PatchMapping("/{code}/activate")
    @Operation(summary = "啟用商品", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<ProductEntity>> activate(@PathVariable String code) {
        return ResponseEntity.ok(ReturnMsg.success(productCommandService.changeStatus(code, "ACTIVE")));
    }

    @PatchMapping("/{code}/deactivate")
    @Operation(summary = "停用商品", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<ProductEntity>> deactivate(@PathVariable String code) {
        return ResponseEntity.ok(ReturnMsg.success(productCommandService.changeStatus(code, "INACTIVE")));
    }

    @PutMapping("/{code}")
    @Operation(summary = "修改商品", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<ReturnMsg<ProductEntity>> update(
            @PathVariable String code,
            @Valid @RequestBody ProductUpdateReqDto dto,
            BindingResult bindingResult) {
        try {
            EntityUtil.validDto(bindingResult);
            ProductEntity updated = productCommandService.update(code, dto);
            return ResponseEntity.ok(ReturnMsg.success(updated));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiCode.SYSTEM_ERROR.getCode(), e.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}