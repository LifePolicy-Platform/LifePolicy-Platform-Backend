package maventest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.mapper.ProductMapper;
import maventest.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public List<ProductEntity> listActiveProducts() {
        return productMapper.findAll().stream()
                .filter(product -> "ACTIVE".equalsIgnoreCase(product.getStatus()))
                .toList();
    }
}
