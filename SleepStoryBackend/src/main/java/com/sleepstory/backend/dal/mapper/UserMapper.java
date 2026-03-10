package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问接口 - MyBatis Mapper
 * 使用XML配置SQL
 */
@Mapper
public interface UserMapper {

    int insert(UserPO user);

    int update(UserPO user);

    UserPO selectById(String id);

    UserPO selectByPhone(String phone);

    UserPO selectByEmail(String email);

    List<UserPO> selectByStatus(String status);

    long countByPhone(String phone);

    long countByEmail(String email);

    int updateStatus(@Param("id") String id, @Param("status") String status);

    int updateLoginInfo(@Param("id") String id, @Param("loginAt") LocalDateTime loginAt, @Param("loginIp") String loginIp);

    int deleteById(String id);
}
