package com.hkct.aiexcel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hkct.aiexcel.entity.ExcelRecord;

/**
 * @author Ethan
 * @description 针对表【excel_record】的数据库操作Mapper
 * @createDate 2024-11-11 21:27:44
 * @Entity com.hkct.aiexcel.entity.ExcelRecord
 */
public interface ExcelRecordMapper extends BaseMapper<ExcelRecord> {

    int deleteByPrimaryKey(Long id);

    int insert(ExcelRecord record);

    int insertSelective(ExcelRecord record);

    ExcelRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExcelRecord record);

    int updateByPrimaryKey(ExcelRecord record);

    int updateStatus(ExcelRecord record);

}
