package maventest.policyapplication.infrastructure.repository.mapper;

import maventest.policyapplication.domain.entity.NotificationEntity;
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
}