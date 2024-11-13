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


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    Logger logger = LoggerFactory.getLogger("com.hkct.aiexcel.Controller.StatusController");

    @Autowired
    private StatusService statusService;


    @PostMapping(value = PathConstants.STATUS)
    public ResponseEntity<Object> updateStatus(@RequestBody UpdateStatusRequest request) throws Exception {
        logger.info("************************************* Start to updateStatus *************************************");

        String status = String.valueOf(request.getStatus().equals(CommonConstants.SAVE) ? 1 : 0);


        try {
            String message = statusService.updateStatus(request.getTemplateId(), status);
            logger.info("************************************* End to updateStatus *************************************");

            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("status", message.equals("1") ? "success" : "fail");
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("status", e.getMessage());
            logger.error("Error in updateStatus: {}", e);
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
