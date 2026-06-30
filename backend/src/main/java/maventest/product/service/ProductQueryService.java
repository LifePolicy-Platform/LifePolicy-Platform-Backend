package maventest.product.service;

import lombok.RequiredArgsConstructor;
import maventest.common.exception.ApiException;
import maventest.product.entity.ProductEntity;
import maventest.product.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductMapper productMapper;

    public List<ProductEntity> findAll() {
        return productMapper.findAll();
    }

    public ProductEntity findByCode(String code) {
        ProductEntity product = productMapper.findByCode(code);
        if (product == null) {
            throw new ApiException("PROD_404", "商品代碼不存在：" + code, HttpStatus.NOT_FOUND);
        }
        return product;
    }
}