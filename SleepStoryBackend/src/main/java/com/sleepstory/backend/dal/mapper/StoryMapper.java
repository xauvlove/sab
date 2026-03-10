package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.StoryPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 故事数据访问接口 - MyBatis Mapper
 */
@Mapper
public interface StoryMapper {

    @Insert({
        "INSERT INTO stories (id, title, description, category, duration, duration_seconds, ",
        "icon, gradient_colors, audio_url, is_generated, is_favorite, rating, play_count, content, ",
        "created_at, updated_at) ",
        "VALUES (#{id}, #{title}, #{description}, #{category}, #{duration}, #{durationSeconds}, ",
        "#{icon}, #{gradientColors}, #{audioUrl}, #{isGenerated}, #{isFavorite}, #{rating}, ",
        "#{playCount}, #{content}, #{createdAt}, #{updatedAt})"
    })
    int insert(StoryPO story);

    @Update({
        "UPDATE stories SET title = #{title}, description = #{description}, category = #{category}, ",
        "duration = #{duration}, duration_seconds = #{durationSeconds}, icon = #{icon}, ",
        "gradient_colors = #{gradientColors}, audio_url = #{audioUrl}, is_generated = #{isGenerated}, ",
        "is_favorite = #{isFavorite}, rating = #{rating}, play_count = #{playCount}, ",
        "content = #{content}, updated_at = #{updatedAt} WHERE id = #{id}"
    })
    int update(StoryPO story);

    @Select("SELECT * FROM stories WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    StoryPO selectById(String id);

    @Select("SELECT * FROM stories WHERE category = #{category} ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<StoryPO> selectByCategory(String category);

    @Select("SELECT * FROM stories WHERE is_generated = false ORDER BY rating DESC, play_count DESC LIMIT #{limit}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<StoryPO> selectRecommendedStories(int limit);

    @Select("SELECT * FROM stories ORDER BY play_count DESC LIMIT #{limit}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<StoryPO> selectTopStories(int limit);

    @Select("SELECT * FROM stories WHERE title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<StoryPO> searchByKeyword(String keyword);

    @Select("SELECT * FROM stories WHERE is_favorite = true ORDER BY updated_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "description", column = "description"),
        @Result(property = "category", column = "category"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationSeconds", column = "duration_seconds"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "gradientColors", column = "gradient_colors"),
        @Result(property = "audioUrl", column = "audio_url"),
        @Result(property = "isGenerated", column = "is_generated"),
        @Result(property = "isFavorite", column = "is_favorite"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "playCount", column = "play_count"),
        @Result(property = "content", column = "content"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<StoryPO> selectFavorites();

    @Update("UPDATE stories SET play_count = play_count + 1 WHERE id = #{id}")
    int incrementPlayCount(String id);

    @Update("UPDATE stories SET is_favorite = #{isFavorite} WHERE id = #{id}")
    int updateFavoriteStatus(@Param("id") String id, @Param("isFavorite") boolean isFavorite);

    @Delete("DELETE FROM stories WHERE id = #{id}")
    int deleteById(String id);
}
