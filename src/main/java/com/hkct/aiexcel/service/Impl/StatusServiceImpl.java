package com.hkct.aiexcel.service.Impl;


import com.hkct.aiexcel.service.StatusService;

import com.hkct.aiexcel.entity.ExcelRecord;
import com.hkct.aiexcel.mapper.ExcelRecordMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StatusServiceImpl implements StatusService {


    @Autowired
    public ExcelRecordMapper excelRecordMapper;

    Logger logger = LoggerFactory.getLogger("com.hkct.aiexcel.Service.Impl.CodeGenerationServiceImpl");


    public String updateStatus(String templateId, String status) throws Exception {
        logger.info("************************************* Start to update status *************************************");
        ExcelRecord selectByPrimaryKey;
        try {

            selectByPrimaryKey = excelRecordMapper.selectByPrimaryKey(templateId);
            selectByPrimaryKey.setStatus(status);
            int statusCode = excelRecordMapper.updateStatus(selectByPrimaryKey);
            logger.info("************************************* End to update status *************************************");
            return String.valueOf(statusCode);

        } catch (Exception e) {

            logger.info("no record for excel, return a default value");
            return "no record for excel";

        }
    }
}
