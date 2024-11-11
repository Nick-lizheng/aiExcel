package com.hkct.aiexcel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private Date createTimestamp;
    private Date updateTimestamp;
    private String status;
    private String templateName;
}