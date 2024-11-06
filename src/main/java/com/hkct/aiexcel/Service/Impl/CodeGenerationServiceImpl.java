package com.hkct.aiexcel.Service.Impl;

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
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkct.aiexcel.Config.ClientConfig;
import com.hkct.aiexcel.Constants.PathConstants;
import com.hkct.aiexcel.Constants.CredentialConstants;
import com.hkct.aiexcel.Service.CodeGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

    Logger logger = Logger.getLogger(CodeGenerationService.class.getName());

    public String generateAndSaveCode(String markdown, String message) throws NoApiKeyException, InputRequiredException {
        logger.info("************************************* Start to generate code *************************************");
        GenerationResult code = generateCode(markdown, message);

        String content = code.getOutput().getChoices().get(0).getMessage().getContent();
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
        saveJavaCodeToFile(javaCode, PathConstants.PATH, "GeneratedCode.java");
        logger.info("************************************* End to generate code *************************************");

        return text;
    }

    private GenerationResult generateCode(String markdown, String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        com.alibaba.dashscope.utils.Constants.apiKey = CredentialConstants.APIKEY;

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content( markdown + message )
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

    public String convertExcel2Markdown(MultipartFile file) throws Exception {
        logger.info("************************************* Start to convert excel to markdown *************************************");


        // Use the ClientConfig to create the client
        Client client = ClientConfig.createClient();

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();
        SubmitDigitalDocStructureJobAdvanceRequest request = new SubmitDigitalDocStructureJobAdvanceRequest();
        request.fileUrlObject = file.getInputStream();
        request.fileName = file.getOriginalFilename();
        request.revealMarkdown = true;


        // 发起请求并处理应答或异常。
        SubmitDigitalDocStructureJobResponse response = client.submitDigitalDocStructureJobAdvance(request, runtime);

        // 从json中分离markdown部分
        try {

            // Convert the response body to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response.getBody().getData());

            // Parse the JSON string
            Map<String, Object> docJson = objectMapper.readValue(jsonResponse, Map.class);

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
                throw new Exception("No layouts found in the response");
            }
            logger.info("************************************* End to convert excel to markdown *************************************");
            // Return the markdown string
            return markdownStr.toString();
        } catch (Exception e) {
            throw new Exception("Error processing the file: " + e.getMessage(), e);
        }


    }
}