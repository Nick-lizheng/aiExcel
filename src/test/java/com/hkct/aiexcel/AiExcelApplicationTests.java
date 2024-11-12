package com.hkct.aiexcel;

import com.hkct.aiexcel.entity.ExcelRecord;
import com.hkct.aiexcel.mapper.ExcelRecordMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiExcelApplicationTests {


    @Autowired
    private ExcelRecordMapper excelRecordMapper;
    @Test
    void contextLoads() {
        ExcelRecord excelRecord = excelRecordMapper.selectByPrimaryKey("053021c3-319c-47c5-95da-dcdfa3dbdb25");
        System.out.println(excelRecord);
    }

}
