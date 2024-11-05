package com.hkct.aiexcel.Controller;


import com.hkct.aiexcel.Constants.PathConstants;
import com.hkct.aiexcel.model.response.FileUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hkct.aiexcel.Service.CodeGenerationService;
import org.springframework.web.multipart.MultipartFile;


import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class genCodeController {

    Logger logger = Logger.getLogger("com.hkct.aiexcelsystem.Controller.genCodeController");

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
