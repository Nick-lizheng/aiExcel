package com.hkct.aiexcel.controller;


import com.hkct.aiexcel.constants.PathConstants;

import com.hkct.aiexcel.entity.ExcelRecord;
import com.hkct.aiexcel.model.request.FileUploadRequest;
import com.hkct.aiexcel.model.respones.SubmitRespones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hkct.aiexcel.service.CodeGenerationService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class GenCodeController {

    Logger logger = Logger.getLogger("com.hkct.aiexcelsystem.Controller.genCodeController");

    @Autowired
    private CodeGenerationService codeGenerationService;


    @PostMapping(value = PathConstants.IMPORT_EXCEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> genCode(@ModelAttribute FileUploadRequest request, HttpServletResponse response) throws Exception {
        logger.info("************************************* Start to import excel *************************************");
        MultipartFile file = request.getFile();

        // save excel file in local path
        MultipartFile multipartFile = request.getFile();
        File destFile = new File("./excel_file",multipartFile.getOriginalFilename());
        try (InputStream inputStream = multipartFile.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("文件保存成功: " + destFile.getAbsolutePath());
        }


        String message = request.getInstruction();
        String markdown;
        try {
            // excel to markdown
            if (Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
               markdown = null;
            }else {
                markdown = codeGenerationService.convertExcel2Markdown(file);
            }


            // markdown to code
            SubmitRespones text = codeGenerationService.generateAndSaveCode(markdown, message, request.isNewConversation());
            logger.info("************************************* End to import excel *************************************");

            return ResponseEntity.ok()
                    .body(text);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = PathConstants.SELECT_EXCEL)
    public ResponseEntity<List<ExcelRecord>> selectTemplate(@RequestBody ExcelRecord request) throws Exception {
        try {
            List<ExcelRecord> record = codeGenerationService.selectTemplate(request);
            return new ResponseEntity<>(record, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = PathConstants.RE_GEN, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> reGenExcel(@ModelAttribute FileUploadRequest request, HttpServletResponse response) throws Exception {
        String path = codeGenerationService.reGen(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=output.xlsx");
        headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);

        InputStreamResource resource = new InputStreamResource(new BufferedInputStream(new FileInputStream(path)));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
