package maventest.policy.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.product.entity.ProductEntity;

@Mapper
public interface ProductListMapper extends BaseMapper<ProductEntity> {
}
