package com.hkct.aiexcel.service;

import com.hkct.aiexcel.model.respones.SubmitRespones;
import org.springframework.web.multipart.MultipartFile;


public interface StatusService {
    String updateStatus(String templateId, String status) throws Exception;
}
