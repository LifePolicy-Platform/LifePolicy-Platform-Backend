package maventest.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import maventest.dto.AptBatchUpdateRequest;
import maventest.dto.AptBatchUpdateResponse;
import maventest.dto.AptUpdateItem;
import maventest.service.AptRecordSingleService;
import maventest.service.CustomerCommandService;

@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final AptRecordSingleService singleService;

    @Override
    public List<AptBatchUpdateResponse> updateAptRecords(AptBatchUpdateRequest request) {

        // ===  進交易前先檢核 + 依安排方式算出要寫入的值  ===
        LocalDate targetDate;
        LocalDateTime specificDateTime = null;   

        switch (request.getMode()) {
            case TODAY:
                targetDate = LocalDate.now();
                break;
            case WORKDAYS:
                if (request.getWorkDays() == null) {
                    throw new IllegalArgumentException("【工作天數】不得為空");
                }
                targetDate = plusWorkDays(LocalDate.now(), request.getWorkDays());
                break;
            case SPECIFIC:
                targetDate = request.getSpecificDateTime().toLocalDate();
                specificDateTime = request.getSpecificDateTime();
                break;
                default:
                throw new IllegalArgumentException("未知的安排方式");
        }

        
        
       String updateUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ===  for 迴圈，逐筆處理 → 驗證（無交易）→ 更新（REQUIRES_NEW） ===
        List<AptBatchUpdateResponse> results = new ArrayList<>();
        for (AptUpdateItem item : request.getRecords()) {
            AptBatchUpdateResponse resp = new AptBatchUpdateResponse();
            resp.setSno(item.getSno());
            try {

                LocalDateTime newRecallTime = singleService.calcNewRecallTime(
                        targetDate, item.getRecallTime(), specificDateTime);

                if (!newRecallTime.isAfter(LocalDateTime.now().plusMinutes(5))) {
                    throw new RuntimeException("約訪時間需大於現在時間 5 分鐘後");
                }

                // 2-3. 執行 DB 更新（REQUIRES_NEW 交易）
                singleService.executeUpdate(item.getListNo(), newRecallTime, updateUser);
                resp.setResult("success");
                resp.setRecallTime(newRecallTime);
            } catch (Exception e) {
                resp.setResult("fail");
                resp.setErrorMsg(e.getMessage());
            }
            results.add(resp);
        }
        return results;
    }

    /** 今日 + N 個工作天（排除週六、週日） */
    private LocalDate plusWorkDays(LocalDate start, int workDays) {
        LocalDate date = start;
        int added = 0;
        while (added < workDays) {
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return date;
    }
}
