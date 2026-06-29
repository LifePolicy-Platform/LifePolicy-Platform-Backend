package maventest.notification.mapper;

import maventest.notification.entity.NotificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    int insert(NotificationEntity entity);

    int countUnreadByUsername(@Param("username") String username);

    List<NotificationEntity> findUnreadByUsername(
            @Param("username") String username,
            @Param("offset") int offset,
            @Param("size") int size);

    int markAsRead(@Param("notifNo") Long notifNo, @Param("username") String username);

    int markAllAsReadByUsername(@Param("username") String username);

    boolean existsSentToday(
            @Param("recipientUsername") String recipientUsername,
            @Param("refNo") String refNo,
            @Param("title") String title);

    int markReadByRefNoAndTitle(
            @Param("refNo") String refNo,
            @Param("title") String title);
}