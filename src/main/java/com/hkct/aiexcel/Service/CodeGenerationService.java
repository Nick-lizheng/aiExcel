package com.hkct.aiexcel.Service;

import com.hkct.aiexcel.model.request.FileUploadRequest;
import com.hkct.aiexcel.model.respones.SubmitRespones;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yangMJ
 * @version 3.0
 * @date 2024-11-05 19:09
 */
public interface CodeGenerationService {
    String convertExcel2Markdown(MultipartFile file) throws Exception;

    SubmitRespones generateAndSaveCode(String markdown, String message) throws Exception;

    String reGen(FileUploadRequest request) throws Exception;
}
