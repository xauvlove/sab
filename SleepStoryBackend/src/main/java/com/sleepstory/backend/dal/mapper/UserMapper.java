package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.UserPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户数据访问接口 - MyBatis Mapper
 */
@Mapper
public interface UserMapper {

    @Insert({
        "INSERT INTO users (id, phone, email, password_hash, nickname, avatar_url, status, ",
        "last_login_at, last_login_ip, created_at, updated_at) ",
        "VALUES (#{id}, #{phone}, #{email}, #{passwordHash}, #{nickname}, #{avatarUrl}, #{status}, ",
        "#{lastLoginAt}, #{lastLoginIp}, #{createdAt}, #{updatedAt})"
    })
    int insert(UserPO user);

    @Update({
        "UPDATE users SET phone = #{phone}, email = #{email}, password_hash = #{passwordHash}, ",
        "nickname = #{nickname}, avatar_url = #{avatarUrl}, status = #{status}, ",
        "last_login_at = #{lastLoginAt}, last_login_ip = #{lastLoginIp}, updated_at = #{updatedAt} ",
        "WHERE id = #{id}"
    })
    int update(UserPO user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "passwordHash", column = "password_hash"),
        @Result(property = "nickname", column = "nickname"),
        @Result(property = "avatarUrl", column = "avatar_url"),
        @Result(property = "status", column = "status"),
        @Result(property = "lastLoginAt", column = "last_login_at"),
        @Result(property = "lastLoginIp", column = "last_login_ip"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    UserPO selectById(String id);

    @Select("SELECT * FROM users WHERE phone = #{phone}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "passwordHash", column = "password_hash"),
        @Result(property = "nickname", column = "nickname"),
        @Result(property = "avatarUrl", column = "avatar_url"),
        @Result(property = "status", column = "status"),
        @Result(property = "lastLoginAt", column = "last_login_at"),
        @Result(property = "lastLoginIp", column = "last_login_ip"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    UserPO selectByPhone(String phone);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "passwordHash", column = "password_hash"),
        @Result(property = "nickname", column = "nickname"),
        @Result(property = "avatarUrl", column = "avatar_url"),
        @Result(property = "status", column = "status"),
        @Result(property = "lastLoginAt", column = "last_login_at"),
        @Result(property = "lastLoginIp", column = "last_login_ip"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    UserPO selectByEmail(String email);

    @Select("SELECT * FROM users WHERE status = #{status}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "passwordHash", column = "password_hash"),
        @Result(property = "nickname", column = "nickname"),
        @Result(property = "avatarUrl", column = "avatar_url"),
        @Result(property = "status", column = "status"),
        @Result(property = "lastLoginAt", column = "last_login_at"),
        @Result(property = "lastLoginIp", column = "last_login_ip"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<UserPO> selectByStatus(String status);

    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    long countByPhone(String phone);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    long countByEmail(String email);

    @Update("UPDATE users SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") String status);

    @Update("UPDATE users SET last_login_at = #{loginAt}, last_login_ip = #{loginIp}, updated_at = NOW() WHERE id = #{id}")
    int updateLoginInfo(@Param("id") String id, @Param("loginAt") java.time.LocalDateTime loginAt, @Param("loginIp") String loginIp);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(String id);
}
