package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.StoryGenerationTaskPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 故事生成任务数据访问接口 - MyBatis Mapper
 * 使用XML配置SQL
 */
@Mapper
public interface StoryGenerationTaskMapper {

    int insert(StoryGenerationTaskPO task);

    int update(StoryGenerationTaskPO task);

    StoryGenerationTaskPO selectById(String id);

    List<StoryGenerationTaskPO> selectByUserId(String userId);

    List<StoryGenerationTaskPO> selectByStatus(String status);

    StoryGenerationTaskPO selectLatestByUserId(String userId);

    long countByStatus(String status);

    int deleteById(String id);

    int deleteOldTasks(int days);
}
