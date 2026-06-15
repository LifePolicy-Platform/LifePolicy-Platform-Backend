package maventest.dto;

/**
 * 重新安排約訪時間的安排方式。
 */
public enum ScheduleMode {

    /** 安排至今日約訪時間（日期改今日，時分秒不變） */
    TODAY,

    /** 安排至今日 + N 個工作天（日期改今日+N，時分秒不變） */
    WORKDAYS,

    /** 安排至指定約訪時間（日期、時間皆由使用者指定） */
    SPECIFIC
}
