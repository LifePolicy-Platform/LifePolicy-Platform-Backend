package maventest.policyapplication.infrastructure.repository.mapper;

import java.util.List;

import maventest.policyapplication.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    ProductEntity findByCode(String productCode);

    List<ProductEntity> findAll();
}