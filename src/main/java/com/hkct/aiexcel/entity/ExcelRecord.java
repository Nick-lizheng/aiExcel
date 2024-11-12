package com.hkct.aiexcel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName excel_record
 */
@Data
@TableName("excel_record")
public class ExcelRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String compliedClassPath;
    private String outputExcelPath;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
    private String status;
    private String templateName;
}