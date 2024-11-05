package com.hkct.aiexcel.model.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUploadRequest {
    private MultipartFile file;
    private String message;

}
