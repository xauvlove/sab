package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.UserPreferencePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户偏好设置数据访问接口
 */
@Mapper
public interface UserPreferenceMapper {

    /**
     * 插入或更新用户偏好设置
     */
    int upsert(UserPreferencePO preference);

    /**
     * 获取用户偏好设置
     */
    UserPreferencePO selectByUserId(@Param("userId") String userId);

    /**
     * 更新深色模式
     */
    int updateDarkMode(@Param("userId") String userId, @Param("darkMode") Boolean darkMode);

    /**
     * 更新自动关闭定时器
     */
    int updateAutoCloseTimer(@Param("userId") String userId, @Param("autoCloseTimer") Integer autoCloseTimer);

    /**
     * 更新音量等级
     */
    int updateVolumeLevel(@Param("userId") String userId, @Param("volumeLevel") Integer volumeLevel);
}
