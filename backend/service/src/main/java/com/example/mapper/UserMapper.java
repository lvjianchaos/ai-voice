package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findUserByName(String username);
@Insert("insert into user(username,password) values(#{username},#{password})")
    void addUser(String username, String password);
}
