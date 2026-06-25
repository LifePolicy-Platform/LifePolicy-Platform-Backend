package maventest.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 單筆約訪時間更新（每筆獨立交易）。
 */
public interface AptRecordSingleService {

    /**
     * 依安排方式計算要寫入的新約訪時間（不查 DB）。
     *
     * @param targetDate         目標日期（TODAY、WORKDAYS）
     * @param originalRecallTime 前端提供的原始約訪時間（TODAY、WORKDAYS 用於保留時分）
     * @param specificDateTime   指定完整時間（SPECIFIC）；null 代表使用 targetDate + 原時分秒
     * @return 計算後的新約訪時間
     */
    LocalDateTime calcNewRecallTime(LocalDate targetDate, LocalDateTime originalRecallTime,
            LocalDateTime specificDateTime);

    /**
     * 執行單筆 DB 更新（REQUIRES_NEW 交易）。
     *
     * @param listNo        名單序號
     * @param newRecallTime 已驗證的新約訪時間
     * @param updateUser    更新者員編
     */
    void executeUpdate(String listNo, LocalDateTime newRecallTime, String updateUser);
}