package com.hkct.aiexcel.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUploadRequest {
    private MultipartFile file;
    private String userId;
    private String instruction;

}
