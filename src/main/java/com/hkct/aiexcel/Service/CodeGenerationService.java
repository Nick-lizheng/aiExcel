package com.hkct.aiexcel.Service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.aliyun.docmind_api20220711.Client;
import com.aliyun.docmind_api20220711.models.SubmitDigitalDocStructureJobAdvanceRequest;
import com.aliyun.docmind_api20220711.models.SubmitDigitalDocStructureJobResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkct.aiexcel.Constants.CommonConstants;
import com.hkct.aiexcel.Constants.CredentialConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CodeGenerationService {

    Logger logger = Logger.getLogger(CodeGenerationService.class.getName());

    public String generateAndSaveCode(String message) throws NoApiKeyException, InputRequiredException {
        GenerationResult code = generateCode(message);

        String content = code.getOutput().getChoices().getFirst().getMessage().getContent();
        logger.info(content);

        // Split the content into text and Java code.
        String[] parts = content.split("```java");
        String text = parts[0].trim();
        String javaCode = "";

        if (parts.length > 1) {
            javaCode = parts[1].split("```")[0].trim();
        } else {
            logger.warning("No Java code found in the generated content.");
        }

        // Save Java code to a file
        saveJavaCodeToFile(javaCode, CommonConstants.PATH, "GeneratedCode.java");

        return text;
    }

    private GenerationResult generateCode(String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        com.alibaba.dashscope.utils.Constants.apiKey = CredentialConstants.APIKEY;

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message)
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(com.alibaba.dashscope.utils.Constants.apiKey)
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

    public ResponseEntity<Object> convertExcel2Markdown(String filePath) throws Exception {

        // 创建Client实例并设置配置

        Config config = new Config()
                .setAccessKeyId(CredentialConstants.ACCESS_KEY_ID)
                // 通过credentials获取配置中的AccessKey Secret
                .setAccessKeySecret(CredentialConstants.ACCESS_KEY_SECRET);
//             访问的域名，支持ipv4和ipv6两种方式，ipv6请使用docmind-api-dualstack.cn-hangzhou.aliyuncs.com

        config.endpoint = "docmind-api.cn-hangzhou.aliyuncs.com";
        Client client = new Client(config);
        // 创建RuntimeObject实例并设置运行参数

        RuntimeOptions runtime = new RuntimeOptions();
        SubmitDigitalDocStructureJobAdvanceRequest request = new SubmitDigitalDocStructureJobAdvanceRequest();
        File file = new File(filePath);
        request.fileUrlObject = new FileInputStream(file);
        request.fileName = file.getName();
        request.revealMarkdown = true;
        // 发起请求并处理应答或异常。
        SubmitDigitalDocStructureJobResponse response = client.submitDigitalDocStructureJobAdvance(request, runtime);


        try {

            // Convert the response body to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response.getBody().getData());

            // Parse the JSON string
            Map<String, Object> docJson = objectMapper.readValue(jsonResponse, Map.class);
//            Map<String, Object> docJson = (Map<String, Object>) responseMap.get("Data");

            // Initialize the markdown string
            StringBuilder markdownStr = new StringBuilder();

            // Check if docJson is not null
            if (docJson != null && docJson.get("layouts") != null) {
                // Iterate over the layouts and append markdown content
                for (Map<String, Object> layout : (Iterable<Map<String, Object>>) docJson.get("layouts")) {
                    markdownStr.append(layout.get("markdownContent")).append("\n");
                }
            } else {
                // Handle the case where docJson or layouts is null
                return new ResponseEntity<>("No layouts found in the response", HttpStatus.BAD_REQUEST);
            }

            // Return the markdown string
            return new ResponseEntity<>(markdownStr.toString(), HttpStatus.OK);
        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}