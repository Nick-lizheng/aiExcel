package com.hkct.aiexcel.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hkct.aiexcel.model.entities.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
