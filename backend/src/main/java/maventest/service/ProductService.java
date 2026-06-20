package maventest.service;

import java.util.List;

import maventest.policyapplication.domain.entity.ProductEntity;


public interface ProductService {
    
    List<ProductEntity> listActiveProducts();
}
