<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/service/impl/AptRecordSingleServiceImpl.java
package maventest.appointment.service.impl;
========
package maventest.visit.service.impl;
>>>>>>>> develop:backend/src/main/java/maventest/visit/service/impl/AptRecordSingleServiceImpl.java

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import lombok.RequiredArgsConstructor;
<<<<<<<< HEAD:backend/src/main/java/maventest/appointment/service/impl/AptRecordSingleServiceImpl.java
import maventest.appointment.entity.AptRecord;
import maventest.appointment.mapper.AptRecordMapper;
import maventest.appointment.service.AptRecordSingleService;
========
import maventest.visit.entity.AptRecord;
import maventest.visit.mapper.AptRecordMapper;
import maventest.visit.service.AptRecordSingleService;
>>>>>>>> develop:backend/src/main/java/maventest/visit/service/impl/AptRecordSingleServiceImpl.java

@Service
@RequiredArgsConstructor
public class AptRecordSingleServiceImpl implements AptRecordSingleService {

    private final AptRecordMapper aptRecordMapper;

    /**
     * 依前端提供的原始約訪時間計算新約訪時間，不查 DB。
     */
    @Override
    public LocalDateTime calcNewRecallTime(LocalDate targetDate, LocalDateTime originalRecallTime,
            LocalDateTime specificDateTime) {
        if (specificDateTime != null) {
            return specificDateTime;
        }

        return LocalDateTime.of(targetDate, originalRecallTime.toLocalTime());
    }

    /**
     * 執行 DB 更新，每筆獨立交易（REQUIRES_NEW）。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void executeUpdate(String listNo, LocalDateTime newRecallTime, String updateUser) {
        LambdaUpdateWrapper<AptRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AptRecord::getRecallTime, newRecallTime)
               .set(AptRecord::getUpdateTime, LocalDateTime.now())
               .set(AptRecord::getUpdateUser, updateUser)
               .eq(AptRecord::getListNo, listNo)
               .isNull(AptRecord::getRecTime);

        int affected = aptRecordMapper.update(wrapper);
        if (affected == 0) {
            throw new RuntimeException("更新失敗，名單序號=" + listNo);
        }
    }
}
