package maventest.product.mapper;

import java.util.List;

import maventest.product.entity.ProductEntity;
import maventest.product.dto.ProductCreateReqDto;
import maventest.product.dto.ProductUpdateReqDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {

    ProductEntity findByCode(@Param("productCode") String productCode);

    ProductEntity findByName(@Param("productName") String productName);

    List<ProductEntity> selectList(LambdaQueryWrapper<ProductEntity> eq);

    List<ProductEntity> findAll();

    int insert(@Param("dto") ProductCreateReqDto dto,
               @Param("createUser") String createUser);

    int updateByCode(@Param("code") String code,
                     @Param("dto") ProductUpdateReqDto dto,
                     @Param("updateUser") String updateUser);

    int updateStatus(@Param("code") String code,
                     @Param("status") String status,
                     @Param("updateUser") String updateUser);
}