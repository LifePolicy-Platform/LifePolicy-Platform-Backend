package maventest.customer.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.customer.dto.ActiveProjectOptionDto;
<<<<<<< HEAD
import maventest.appointment.dto.AptRecordListRequest;
import maventest.appointment.dto.AptRecordListResponse;
import maventest.appointment.dto.CallAppointmentItemDto;
import maventest.appointment.dto.PolicyAppointmentContextDto;
=======
import maventest.visit.dto.AptRecordListRequest;
import maventest.visit.dto.AptRecordListResponse;
import maventest.visit.dto.CallAppointmentItemDto;
import maventest.visit.dto.PolicyAppointmentContextDto;
>>>>>>> develop
import maventest.customer.service.CustomerQueryService;

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

    @Operation(summary = "約訪歷程查詢", description = "依身分證字號查詢約訪記錄")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<AptRecordListResponse>>> searchHistory(
            @RequestParam String identityCard) {
        return ResponseEntity.ok(ApiResponse.ok(customerQueryService.listAptRecordsByIdentityCard(identityCard)));
    }

    @Operation(summary = "保單約訪上下文", description = "依保單編號取得名單序號、客戶姓名、撥出電話等自動帶入欄位")
    @GetMapping("/policy-context")
    public ResponseEntity<ApiResponse<PolicyAppointmentContextDto>> getPolicyContext(
            @RequestParam String policyNo) {
        return ResponseEntity.ok(ApiResponse.ok(customerQueryService.getPolicyAppointmentContext(policyNo)));
    }

    @Operation(summary = "有效專案選項", description = "取得服務期限內的專案清單")
    @GetMapping("/active-projects")
    public ResponseEntity<ApiResponse<List<ActiveProjectOptionDto>>> listActiveProjects() {
        return ResponseEntity.ok(ApiResponse.ok(customerQueryService.listActiveProjects()));
    }

    @Operation(summary = "名單約訪歷程", description = "依名單序號查詢 tb_call_appointment 約訪記錄")
    @GetMapping("/by-list/{listNo}")
    public ResponseEntity<ApiResponse<List<CallAppointmentItemDto>>> listByListNo(
            @PathVariable String listNo) {
        return ResponseEntity.ok(ApiResponse.ok(customerQueryService.listAppointmentsByListNo(listNo)));
    }
}
