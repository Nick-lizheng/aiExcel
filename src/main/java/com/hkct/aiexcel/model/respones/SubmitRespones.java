package com.hkct.aiexcel.model.respones;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmitRespones {


    private String outputFileUrl;
    private String template_id;
    private String message;
    private String excelResponse;


}
