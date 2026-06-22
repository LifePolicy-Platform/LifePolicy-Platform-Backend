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
import maventest.dto.CallAppointmentConfirmRequest;
import maventest.dto.CallAppointmentConfirmResponse;
import maventest.dto.CallAppointmentCreateRequest;
import maventest.dto.CallAppointmentCreateResponse;
import maventest.dto.UserDetailUpdateRequest;
import maventest.dto.UserDetailUpdateResponse;
import maventest.service.CustomerCommandService;


@Tag(name = "異動約訪功能", description = "提供約訪時間的查詢、新增、修改功能")
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerCommandController {
    
    private final CustomerCommandService customerCommandService;

    @Operation(summary = "異動約訪記錄檔")
    @PostMapping("/updateAppoint")
    public ResponseEntity<ApiResponse<List<AptBatchUpdateResponse>>> batchUpdate(
            @Valid @RequestBody AptBatchUpdateRequest request) {
        List<AptBatchUpdateResponse> result = customerCommandService.updateAptRecords(request);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "新增約訪", description = "於保單對應名單新增 tb_call_appointment 約訪記錄")
    @PostMapping("/createAppoint")
    public ResponseEntity<ApiResponse<CallAppointmentCreateResponse>> createAppointment(
            @Valid @RequestBody CallAppointmentCreateRequest request) {
        CallAppointmentCreateResponse result = customerCommandService.createAppointment(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "確認約訪結果", description = "約訪承辦人確認成功或失敗，並將名單結案")
    @PostMapping("/confirmAppointResult")
    public ResponseEntity<ApiResponse<CallAppointmentConfirmResponse>> confirmAppointmentResult(
            @Valid @RequestBody CallAppointmentConfirmRequest request) {
        CallAppointmentConfirmResponse result = customerCommandService.confirmAppointmentResult(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // @Operation(summary = "更新個人資料", description = "更新客戶個人資料，包含姓名、聯絡電話、電子郵件等")
    // @PostMapping("/updatePersonalInfo")
    // public ResponseEntity<ApiResponse<UserDetailUpdateResponse>> updatePersonalInfo(
    //         @Valid @RequestBody UserDetailUpdateRequest request) {
    //     UserDetailUpdateResponse result = customerCommandService.updatePersonalInfo(request);
    //     return ResponseEntity.ok(ApiResponse.ok(result));
    // }
}
