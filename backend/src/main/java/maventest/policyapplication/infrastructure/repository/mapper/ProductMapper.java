package maventest.policyapplication.infrastructure.repository.mapper;

import java.util.List;

import maventest.policyapplication.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {

    ProductEntity findByCode(@Param("productCode") String productCode);

    List<ProductEntity> selectList(LambdaQueryWrapper<ProductEntity> eq);

    List<ProductEntity> findAll();
}