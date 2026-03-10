package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.UserProfilePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户资料数据访问接口 - MyBatis Mapper
 * 使用XML配置SQL
 */
@Mapper
public interface UserProfileMapper {
    int insert(UserProfilePO profile);
    int update(UserProfilePO profile);
    UserProfilePO selectByUserId(String userId);
    int incrementStreak(String userId);
    int resetStreak(String userId);
    int recordListening(@Param("userId") String userId, @Param("minutes") int minutes);
    int deleteByUserId(String userId);

    /**
     * 更新总听书时长
     */
    int updateTotalListeningMinutes(@Param("userId") String userId, @Param("minutes") int minutes);

    /**
     * 更新听完故事数
     */
    int updateTotalStoriesListened(@Param("userId") String userId, @Param("count") int count);
}
