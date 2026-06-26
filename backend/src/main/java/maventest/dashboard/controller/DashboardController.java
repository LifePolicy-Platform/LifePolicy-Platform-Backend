package maventest.dashboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.code.ApiCode;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.dashboard.dto.DashboardSummaryRespDto;
import maventest.dashboard.service.DashboardQueryService;

@Tag(name = "Dashboard", description = "首頁 Dashboard 統計 API")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardQueryService dashboardQueryService;

    @GetMapping("/summary")
    @Operation(
            summary = "取得 Dashboard 彙總資料",
            description = """
                    今年業績：POLICY_STATUS=APPROVED 且 EFFECT_DATE 在今年（趨勢為去年同期比較）。
                    月目標達成：本月核准件數 / 設定目標。
                    當月申請保單：依 APPLY_TIME 當月每日統計。
                    保單狀態分布：今年 APPLY_TIME 分組。
                    商品種類銷售量：今年核准件數依 PRODUCT_TYPE 分組。
                    右側三卡：待審保單、當月理賠申請、SLA 逾期案件。
                    """,
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<DashboardSummaryRespDto>> getSummary() {
        try {
            DashboardSummaryRespDto summary = dashboardQueryService.getSummary();
            return ResponseEntity.ok(ReturnMsg.success(summary));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException(
                    ApiCode.SYSTEM_ERROR.getCode(),
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
