package com.hkct.aiexcel.service;

import com.hkct.aiexcel.model.request.FileUploadRequest;
import com.hkct.aiexcel.entity.ExcelRecord;
import com.hkct.aiexcel.model.respones.SubmitRespones;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author yangMJ
 * @version 3.0
 * @date 2024-11-05 19:09
 */
public interface CodeGenerationService {
    String convertExcel2Markdown(MultipartFile file) throws Exception;

    SubmitRespones generateAndSaveCode(String markdown, String message,boolean newConversation) throws Exception;

    List<ExcelRecord> selectTemplate(ExcelRecord request);

    String reGen(FileUploadRequest request) throws Exception;
}
