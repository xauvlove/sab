package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.UserProfilePO;
import org.apache.ibatis.annotations.*;

/**
 * 用户资料数据访问接口 - MyBatis Mapper
 */
@Mapper
public interface UserProfileMapper {

    @Insert({
        "INSERT INTO user_profiles (user_id, sleep_time, preferred_duration, preferred_categories, ",
        "streak_days, total_stories_listened, total_listening_minutes, last_listened_at, created_at, updated_at) ",
        "VALUES (#{userId}, #{sleepTime}, #{preferredDuration}, #{preferredCategories}, ",
        "#{streakDays}, #{totalStoriesListened}, #{totalListeningMinutes}, #{lastListenedAt}, ",
        "#{createdAt}, #{updatedAt})"
    })
    int insert(UserProfilePO profile);

    @Update({
        "UPDATE user_profiles SET sleep_time = #{sleepTime}, preferred_duration = #{preferredDuration}, ",
        "preferred_categories = #{preferredCategories}, streak_days = #{streakDays}, ",
        "total_stories_listened = #{totalStoriesListened}, total_listening_minutes = #{totalListeningMinutes}, ",
        "last_listened_at = #{lastListenedAt}, updated_at = #{updatedAt} ",
        "WHERE user_id = #{userId}"
    })
    int update(UserProfilePO profile);

    @Select("SELECT * FROM user_profiles WHERE user_id = #{userId}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "sleepTime", column = "sleep_time"),
        @Result(property = "preferredDuration", column = "preferred_duration"),
        @Result(property = "preferredCategories", column = "preferred_categories"),
        @Result(property = "streakDays", column = "streak_days"),
        @Result(property = "totalStoriesListened", column = "total_stories_listened"),
        @Result(property = "totalListeningMinutes", column = "total_listening_minutes"),
        @Result(property = "lastListenedAt", column = "last_listened_at"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    UserProfilePO selectByUserId(String userId);

    @Update("UPDATE user_profiles SET streak_days = streak_days + 1, updated_at = NOW() WHERE user_id = #{userId}")
    int incrementStreak(String userId);

    @Update("UPDATE user_profiles SET streak_days = 0, updated_at = NOW() WHERE user_id = #{userId}")
    int resetStreak(String userId);

    @Update({
        "UPDATE user_profiles SET total_stories_listened = total_stories_listened + 1, ",
        "total_listening_minutes = total_listening_minutes + #{minutes}, ",
        "last_listened_at = NOW(), updated_at = NOW() WHERE user_id = #{userId}"
    })
    int recordListening(@Param("userId") String userId, @Param("minutes") int minutes);

    @Delete("DELETE FROM user_profiles WHERE user_id = #{userId}")
    int deleteByUserId(String userId);
}
