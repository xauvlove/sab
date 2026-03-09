package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.StoryGenerationTaskPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 故事生成任务数据访问接口 - MyBatis Mapper
 */
@Mapper
public interface StoryGenerationTaskMapper {

    @Insert({
        "INSERT INTO story_generation_tasks (id, user_id, keywords, scene, mood, duration, ",
        "status, story_id, error_message, progress_percent, status_message, ",
        "created_at, updated_at, completed_at) ",
        "VALUES (#{id}, #{userId}, #{keywords}, #{scene}, #{mood}, #{duration}, ",
        "#{status}, #{storyId}, #{errorMessage}, #{progressPercent}, #{statusMessage}, ",
        "#{createdAt}, #{updatedAt}, #{completedAt})"
    })
    int insert(StoryGenerationTaskPO task);

    @Update({
        "UPDATE story_generation_tasks SET user_id = #{userId}, keywords = #{keywords}, ",
        "scene = #{scene}, mood = #{mood}, duration = #{duration}, status = #{status}, ",
        "story_id = #{storyId}, error_message = #{errorMessage}, progress_percent = #{progressPercent}, ",
        "status_message = #{statusMessage}, updated_at = #{updatedAt}, completed_at = #{completedAt} ",
        "WHERE id = #{id}"
    })
    int update(StoryGenerationTaskPO task);

    @Select("SELECT * FROM story_generation_tasks WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "keywords", column = "keywords"),
        @Result(property = "scene", column = "scene"),
        @Result(property = "mood", column = "mood"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "status", column = "status"),
        @Result(property = "storyId", column = "story_id"),
        @Result(property = "errorMessage", column = "error_message"),
        @Result(property = "progressPercent", column = "progress_percent"),
        @Result(property = "statusMessage", column = "status_message"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "completedAt", column = "completed_at")
    })
    StoryGenerationTaskPO selectById(String id);

    @Select("SELECT * FROM story_generation_tasks WHERE user_id = #{userId} ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "keywords", column = "keywords"),
        @Result(property = "scene", column = "scene"),
        @Result(property = "mood", column = "mood"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "status", column = "status"),
        @Result(property = "storyId", column = "story_id"),
        @Result(property = "errorMessage", column = "error_message"),
        @Result(property = "progressPercent", column = "progress_percent"),
        @Result(property = "statusMessage", column = "status_message"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "completedAt", column = "completed_at")
    })
    List<StoryGenerationTaskPO> selectByUserId(String userId);

    @Select("SELECT * FROM story_generation_tasks WHERE status = #{status} ORDER BY created_at ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "keywords", column = "keywords"),
        @Result(property = "scene", column = "scene"),
        @Result(property = "mood", column = "mood"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "status", column = "status"),
        @Result(property = "storyId", column = "story_id"),
        @Result(property = "errorMessage", column = "error_message"),
        @Result(property = "progressPercent", column = "progress_percent"),
        @Result(property = "statusMessage", column = "status_message"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "completedAt", column = "completed_at")
    })
    List<StoryGenerationTaskPO> selectByStatus(String status);

    @Select("SELECT * FROM story_generation_tasks WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "keywords", column = "keywords"),
        @Result(property = "scene", column = "scene"),
        @Result(property = "mood", column = "mood"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "status", column = "status"),
        @Result(property = "storyId", column = "story_id"),
        @Result(property = "errorMessage", column = "error_message"),
        @Result(property = "progressPercent", column = "progress_percent"),
        @Result(property = "statusMessage", column = "status_message"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "completedAt", column = "completed_at")
    })
    StoryGenerationTaskPO selectLatestByUserId(String userId);

    @Select("SELECT COUNT(*) FROM story_generation_tasks WHERE status = #{status}")
    long countByStatus(String status);

    @Delete("DELETE FROM story_generation_tasks WHERE id = #{id}")
    int deleteById(String id);

    @Delete("DELETE FROM story_generation_tasks WHERE created_at < DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    int deleteOldTasks(int days);
}
