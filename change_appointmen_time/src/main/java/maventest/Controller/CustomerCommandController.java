package maventest.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.common.config.OpenApiConfig;
import maventest.dto.AptBatchUpdateResponse;
import maventest.dto.AptBatchUpdateRequest;
import maventest.service.CustomerCommandService;


@Tag(name = "異動約訪功能", description = "提供約訪時間的查詢、新增、修改功能")
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerCommandController {
    
     private final CustomerCommandService customerCommandService;

    @Operation(summary  = "異動約訪記錄檔")
    @PostMapping("/updateAppoint")
    public ResponseEntity<ApiResponse<List<AptBatchUpdateResponse>>> batchUpdate
        (@Valid @RequestBody AptBatchUpdateRequest request) {
        List<AptBatchUpdateResponse> result = customerCommandService.updateAptRecords(request);
        
         return ResponseEntity.ok(ApiResponse.ok(result));
    }
    
}
