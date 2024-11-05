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
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkct.aiexcel.Config.ClientConfig;
import com.hkct.aiexcel.Constants.PathConstants;
import com.hkct.aiexcel.Constants.CredentialConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CodeGenerationService {

    Logger logger = Logger.getLogger(CodeGenerationService.class.getName());

    public String generateAndSaveCode(String markdown, String message) throws NoApiKeyException, InputRequiredException {
        logger.info("************************************* Start to generate code *************************************");
        GenerationResult code = generateCode(markdown, message);

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
        saveJavaCodeToFile(javaCode, PathConstants.PATH, "GeneratedCode.java");
        logger.info("************************************* End to generate code *************************************");

        return text;
    }

    private GenerationResult generateCode(String markdown, String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        com.alibaba.dashscope.utils.Constants.apiKey = CredentialConstants.APIKEY;

        String original_excel_path = PathConstants.ORIGINAL_EXCEL_PATH;
        String output_excel_path = PathConstants.OUTPUT_EXCEL_PATH;

        String prompt = "任务指令：根据已解析成Markdown格式的Excel文件内容，编写Java代码以执行刚刚我描述的Excel操作，并生成新的Excel文档。\n\n" +
                "背景信息：\n" +
                "- 原始Excel文件路径: '" + original_excel_path + "'\n" +
                "- 目标输出Excel文件路径: '" + output_excel_path + "'\n\n" +
                "具体要求：\n" +
                "- 读取位于'" + original_excel_path + "'的Excel文件。\n" +
                "- 解析并理解提供的Markdown文件中的数据和操作说明。\n" +
                "- 根据Markdown文件中描述的操作，在原始Excel文件上执行相应处理。\n" +
                "- 将处理后的结果保存为一个新的Excel文件，存储于'" + output_excel_path + "'。\n\n" +
                "注意事项：\n" +
                "1. Java代码需要能够准确地反映Markdown文件中定义的所有操作步骤。\n" +
                "2. 确保最终生成的Excel文件符合Markdown文件中指定的所有要求。\n" +
                "3. 如果在实现过程中遇到任何问题或不确定的地方，请明确指出并提供可能的解决方案或建议。\n\n" +
                "请基于上述要求生成详细的Java代码示例。如果还有其他特定需求或者偏好（例如使用特定库来处理Excel），请通过设置以下变量告知：\n" +
                "- `${preferred_library_for_excel_handling}` (例如Apache POI, JXL等)\n" +
                "- `${additional_requirements}` (如果有额外的需求或限制条件)";

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content( markdown + message +prompt )
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