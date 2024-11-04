package com.hkct.aiexcel.Controller;


import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.hkct.aiexcel.Constants.CommonConstants;
import com.hkct.aiexcel.Constants.CredentialConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class genCodeController {

    Logger logger = Logger.getLogger("com.hkct.aiexcelsystem.Controller.genCodeController");


    @PostMapping("/genCode")
    public ResponseEntity<Object> handleFileUpload(@RequestBody String message) {

        logger.info("************************************* Start to generate code *************************************");


//        if (file.isEmpty()) {
//            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
//        }

        try {

            //conver excel to markdown
            // 使用默认凭证初始化Credentials Client。
//            com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client();
//
//            Config config = new Config()
//                    .accessKeyId(credentialClient.getAccessKeyId())
//                    // 通过credentials获取配置中的AccessKey ID
//                    .setAccessKeyId(credentialClient.getAccessKeyId())
//                    // 通过credentials获取配置中的AccessKey Secret
//                    .setAccessKeySecret(credentialClient.getAccessKeySecret());
            // 访问的域名，支持ipv4和ipv6两种方式，ipv6请使用docmind-api-dualstack.cn-hangzhou.aliyuncs.com





            // 调用通义千问API生成代码
            GenerationResult code = generateCode(message);

            String content = code.getOutput().getChoices().get(0).getMessage().getContent();
            logger.info(content);


            // Split the content into text and Java code.
            String[] parts = content.split("```java");
            String text = parts[0].trim();
            String javaCode = parts[1].split("```")[0].trim();


            // Save Java code to a file
            saveJavaCodeToFile(javaCode, CommonConstants.PATH, "GeneratedCode.java");


            logger.info("************************************* End to generate code *************************************");
            return new ResponseEntity<>(text, HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private GenerationResult generateCode(String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Constants.apiKey = CredentialConstants.APIKEY;//这里填写自己申请的APIKEY

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();


        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
//                .content("帮我生成一段Java代码，使用EasyExcel库读取一个Excel文件内容，并计算columnD 加上 columnE , 把对应的结果放在columnF。文件内容为" + file)
                .content(message)
                .build();

        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
//                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .apiKey(Constants.apiKey)
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();


        return gen.call(param);
    }

    private void saveJavaCodeToFile(String javaCode, String path, String fileName) {
        try (FileWriter fileWriter = new FileWriter(path + fileName)) {
            fileWriter.write(javaCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
