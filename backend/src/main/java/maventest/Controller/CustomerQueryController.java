package maventest.Controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.dto.AptRecordListRequest;
import maventest.dto.AptRecordListResponse;
import maventest.service.CustomerQueryService;

@Tag(name = "約訪名單管理")
@RestController
@RequestMapping("/api/apt-records")
@RequiredArgsConstructor
public class CustomerQueryController {

    private final CustomerQueryService customerQueryService;

    @Operation(summary = "約訪名單查詢", description = "依起始/結束日期與時間區間查詢約訪名單")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AptRecordListResponse>>> search(
            @Valid @ParameterObject @ModelAttribute AptRecordListRequest request) {

            List<AptRecordListResponse> data = customerQueryService.listAptRecords(request);    

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

   
}
