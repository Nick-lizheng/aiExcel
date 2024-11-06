package com.hkct.aiexcel.model.entities;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("excel_user")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    private String userName;
}