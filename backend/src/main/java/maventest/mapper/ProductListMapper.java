package maventest.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import maventest.policyapplication.domain.entity.ProductEntity;

@Mapper
public interface ProductListMapper extends BaseMapper<ProductEntity> {
}
