package com.hkct.aiexcel.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hkct.aiexcel.Service.CodeGenerationService;


import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class genCodeController {

    Logger logger = Logger.getLogger("com.hkct.aiexcelsystem.Controller.genCodeController");

    @Autowired
    private CodeGenerationService codeGenerationService;


    @PostMapping("/genCode")
    public ResponseEntity<Object> handleFileUpload(@RequestBody String message) {

        logger.info("************************************* Start to generate code *************************************");
        try {
            String text = codeGenerationService.generateAndSaveCode(message);

            logger.info("************************************* End to generate code *************************************");
            return new ResponseEntity<>(text, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/convertExcel2Markdown")
    public ResponseEntity<Object> convertExcel2Markdown(@RequestBody String filePath) {
        logger.info("************************************* Start to convert excel to markdown *************************************");

        try {
            logger.info("************************************* End to convert excel to markdown *************************************");
            return codeGenerationService.convertExcel2Markdown(filePath);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
