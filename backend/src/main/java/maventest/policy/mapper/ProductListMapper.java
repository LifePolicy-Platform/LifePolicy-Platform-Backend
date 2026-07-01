<<<<<<<< HEAD:backend/src/main/java/maventest/policyapplication/infrastructure/repository/mapper/ProductListMapper.java
package maventest.policyapplication.infrastructure.repository.mapper;
========
package maventest.policy.mapper;
>>>>>>>> develop:backend/src/main/java/maventest/policy/mapper/ProductListMapper.java

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.product.entity.ProductEntity;

@Mapper
public interface ProductListMapper extends BaseMapper<ProductEntity> {
}
