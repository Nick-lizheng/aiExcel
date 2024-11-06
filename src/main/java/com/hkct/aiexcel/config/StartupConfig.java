package com.hkct.aiexcel.config;
import javax.annotation.PostConstruct;

import com.hkct.aiexcel.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class StartupConfig {

    @Autowired
    private UserService userService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(StartupConfig.class);

    @PostConstruct
    public void init() {

        logger.info("************************************* Start to init *************************************");


        logger.info("user: {}",userService.getAllUsers());

        logger.info("************************************* End to init *************************************");
    }

}
