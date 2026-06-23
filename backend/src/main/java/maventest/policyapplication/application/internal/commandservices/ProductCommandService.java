package maventest.policyapplication.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.common.exception.ErrorInputException;
import maventest.policyapplication.domain.entity.ProductEntity;
import maventest.policyapplication.infrastructure.repository.mapper.ProductMapper;
import maventest.policyapplication.interfaces.dto.ProductCreateReqDto;
import maventest.policyapplication.interfaces.dto.ProductUpdateReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private static final Set<String> VALID_TYPES   = Set.of("LIFE", "HEALTH", "ACCIDENT", "ANNUITY", "TRAVEL");
    private static final Set<String> VALID_STATUSES = Set.of("ACTIVE", "INACTIVE");

    private final ProductMapper productMapper;

    public ProductEntity create(ProductCreateReqDto dto) {
        if (productMapper.findByCode(dto.getProductCode()) != null) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_CODE_DUPLICATE.getCode(),
                    ApiCode.PRODUCT_CODE_DUPLICATE.getMessage());
        }
        if (productMapper.findByName(dto.getProductName()) != null) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_NAME_DUPLICATE.getCode(),
                    ApiCode.PRODUCT_NAME_DUPLICATE.getMessage());
        }
        validateTypeAndRanges(dto.getProductType(),
                dto.getMinAmount(), dto.getMaxAmount(),
                dto.getMinAge(), dto.getMaxAge());

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        productMapper.insert(dto, currentUser);
        return productMapper.findByCode(dto.getProductCode());
    }

    public ProductEntity update(String code, ProductUpdateReqDto dto) {
        if (productMapper.findByCode(code) == null) {
            throw new ApiException(
                    ApiCode.PRODUCT_NOT_FOUND.getCode(),
                    ApiCode.PRODUCT_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND);
        }

        ProductEntity existing = productMapper.findByName(dto.getProductName());
        if (existing != null && !existing.getProductCode().equals(code)) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_NAME_DUPLICATE.getCode(),
                    ApiCode.PRODUCT_NAME_DUPLICATE.getMessage());
        }
        validateDto(dto);

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        int updated = productMapper.updateByCode(code, dto, currentUser);
        if (updated == 0) {
            throw new ApiException(
                    ApiCode.SYSTEM_ERROR.getCode(),
                    "Update failed for product: " + code,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return productMapper.findByCode(code);
    }

    public ProductEntity changeStatus(String code, String targetStatus) {
        if (productMapper.findByCode(code) == null) {
            throw new ApiException(
                    ApiCode.PRODUCT_NOT_FOUND.getCode(),
                    ApiCode.PRODUCT_NOT_FOUND.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        if (!VALID_STATUSES.contains(targetStatus)) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_STATUS_INVALID.getCode(),
                    ApiCode.PRODUCT_STATUS_INVALID.getMessage());
        }
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        productMapper.updateStatus(code, targetStatus, currentUser);
        return productMapper.findByCode(code);
    }

    private void validateDto(ProductUpdateReqDto dto) {
        if (!VALID_STATUSES.contains(dto.getStatus())) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_STATUS_INVALID.getCode(),
                    ApiCode.PRODUCT_STATUS_INVALID.getMessage());
        }
        validateTypeAndRanges(dto.getProductType(),
                dto.getMinAmount(), dto.getMaxAmount(),
                dto.getMinAge(), dto.getMaxAge());
    }

    private void validateTypeAndRanges(String productType,
                                       BigDecimal minAmount, BigDecimal maxAmount,
                                       Integer minAge, Integer maxAge) {
        if (!VALID_TYPES.contains(productType)) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_TYPE_INVALID.getCode(),
                    ApiCode.PRODUCT_TYPE_INVALID.getMessage());
        }
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_AMOUNT_RANGE_INVALID.getCode(),
                    ApiCode.PRODUCT_AMOUNT_RANGE_INVALID.getMessage());
        }
        if (minAge > maxAge) {
            throw new ErrorInputException(
                    ApiCode.PRODUCT_AGE_RANGE_INVALID.getCode(),
                    ApiCode.PRODUCT_AGE_RANGE_INVALID.getMessage());
        }
    }
}