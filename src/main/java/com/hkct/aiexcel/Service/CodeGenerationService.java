package com.hkct.aiexcel.Service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yangMJ
 * @version 3.0
 * @date 2024-11-05 19:09
 */
public interface CodeGenerationService {
    String convertExcel2Markdown(MultipartFile file) throws Exception;

    String generateAndSaveCode(String markdown, String message) throws NoApiKeyException, InputRequiredException;
}
