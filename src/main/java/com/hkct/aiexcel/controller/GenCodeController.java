package com.hkct.aiexcel.controller;


import com.hkct.aiexcel.config.StartupConfig;
import com.hkct.aiexcel.constants.PathConstants;
import com.hkct.aiexcel.model.response.FileUploadRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hkct.aiexcel.service.CodeGenerationService;
import org.springframework.web.multipart.MultipartFile;




@RestController
@RequestMapping("/api")
public class GenCodeController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(GenCodeController.class);


    @Autowired
    private CodeGenerationService codeGenerationService;


    @PostMapping(value = PathConstants.IMPORT_EXCEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> genCode(@ModelAttribute FileUploadRequest request) throws Exception {
        logger.info("************************************* Start to import excel *************************************");
        MultipartFile file = request.getFile();

        String message = request.getMessage();
        try {
            //excel to markdown
            String markdown = codeGenerationService.convertExcel2Markdown(file);

            //markdown to code
            String text = codeGenerationService.generateAndSaveCode(markdown, message);

            logger.info("************************************* End to import excel *************************************");
            return new ResponseEntity<>(text, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
