package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.StoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 故事数据访问接口 - MyBatis Mapper
 * 使用XML配置SQL
 */
@Mapper
public interface StoryMapper {

    int insert(StoryPO story);

    int update(StoryPO story);

    StoryPO selectById(String id);

    List<StoryPO> selectByCategory(String category);

    List<StoryPO> selectRecommendedStories(int limit);

    List<StoryPO> selectTopStories(int limit);

    List<StoryPO> searchByKeyword(String keyword);

    List<StoryPO> selectFavorites();

    int incrementPlayCount(String id);

    int updateFavoriteStatus(@Param("id") String id, @Param("isFavorite") boolean isFavorite);

    int deleteById(String id);
}
