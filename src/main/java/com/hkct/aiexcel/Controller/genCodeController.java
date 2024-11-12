package com.hkct.aiexcel.Controller;


import com.hkct.aiexcel.Service.CodeGenerationService;
import com.hkct.aiexcel.constants.PathConstants;
import com.hkct.aiexcel.model.request.FileUploadRequest;
import com.hkct.aiexcel.model.respones.SubmitRespones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class genCodeController {

    Logger logger = Logger.getLogger("com.hkct.aiexcelsystem.Controller.genCodeController");

    @Autowired
    private CodeGenerationService codeGenerationService;


    @PostMapping(value = PathConstants.IMPORT_EXCEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> genCode(@ModelAttribute FileUploadRequest request, HttpServletResponse response) throws Exception {
        logger.info("************************************* Start to import excel *************************************");
        MultipartFile file = request.getFile();

        String message = request.getInstruction();
        try {
            // excel to markdown
            String markdown = codeGenerationService.convertExcel2Markdown(file);

            // markdown to code
            SubmitRespones text = codeGenerationService.generateAndSaveCode(markdown, message);
            logger.info("************************************* End to import excel *************************************");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=output.xlsx");
            headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add("template_id", text.getTemplate_id());
            headers.add("excelResponse", text.getMessage());


            InputStreamResource resource =
                    new InputStreamResource(new BufferedInputStream(new FileInputStream("./excel_file/output.xlsx")));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping(PathConstants.RE_GEN)
    // public ResponseEntity<Object> reGenExcel(FileUploadRequest request, HttpServletResponse response) throws Exception {
    //     String path = codeGenerationService.reGen(request);
    //
    //
    //
    //     return new ResponseEntity<>(text, HttpStatus.OK);
    // }


}