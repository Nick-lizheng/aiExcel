package com.hkct.aiexcel.controller;


import com.hkct.aiexcel.service.StatusService;
import com.hkct.aiexcel.constants.CommonConstants;
import com.hkct.aiexcel.constants.PathConstants;
import com.hkct.aiexcel.model.request.UpdateStatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static java.awt.SystemColor.text;

@RestController
@RequestMapping("/api")
public class StatusController {

    Logger logger = LoggerFactory.getLogger("com.hkct.aiexcel.Controller.StatusController");

    @Autowired
    private StatusService statusService;


    @PostMapping(value = PathConstants.STATUS)
    public ResponseEntity<Object> updateStatus(@RequestBody UpdateStatusRequest request) throws Exception {
        logger.info("************************************* Start to updateStatus *************************************");


        try {
            String message = statusService.updateStatus(request.getTemplateId(), request.getStatus());
            logger.info("************************************* End to updateStatus *************************************");
            String responseMessage = "1".equals(message) ? CommonConstants.STORE : CommonConstants.OBSOLETE;
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error in updateStatus: {}", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
