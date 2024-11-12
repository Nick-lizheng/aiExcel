package com.hkct.aiexcel.service.Impl;

import cn.hutool.core.util.IdUtil;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkct.aiexcel.config.ClientConfig;
import com.hkct.aiexcel.service.CodeGenerationService;
import com.hkct.aiexcel.utils.JavaToClassFile;
import com.hkct.aiexcel.constants.CredentialConstants;
import com.hkct.aiexcel.constants.PromptConstants;
import com.hkct.aiexcel.entity.ExcelRecord;
import com.hkct.aiexcel.mapper.ExcelRecordMapper;
import com.hkct.aiexcel.model.request.FileUploadRequest;
import com.hkct.aiexcel.model.respones.SubmitRespones;
import javassist.ClassPool;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {


    @Autowired
    private ExcelRecordMapper excelRecordMapper;

    Logger logger = LoggerFactory.getLogger("com.hkct.aiexcel.Service.Impl.CodeGenerationServiceImpl");

    public SubmitRespones generateAndSaveCode(String markdown, String message) throws Exception {
        logger.info("************************************* Start to generate code *************************************");
        PromptConstants.PROMPT = updateExcelName();
        System.out.println(PromptConstants.PROMPT);
        System.out.println(PromptConstants.getUserPrompt());
        GenerationResult code = generateCode(markdown, message);

        String content = code.getOutput().getChoices().get(0).getMessage().getContent();
        logger.info(content);

        // Split the content into text and Java code.
        String[] parts = content.split("```java");
        String text = "";
        String javaCode = "";
        if (parts.length > 1) {
            javaCode = parts[1].split("```")[0].trim();
            text = parts[1].split("```")[1].trim();
        } else {
            logger.error("No Java code found in the generated content.");
        }
//        CommonOssUtils.saveJavaCodeToOss(javaCode, objectName);
        // Save Java code to a file
        String fileName = PromptConstants.PROMPT + ".java";
        String filePathName = "./gen_src_code/"+fileName;
        saveJavaCodeToFile(javaCode, "./gen_src_code/", fileName);
        logger.info("************************************* End to generate code *************************************");

        logger.info("compile java file");
        JavaToClassFile.compileToClassFile(filePathName);
        logger.info("load class and run to generate excel");
        String excelResponse = loadClassAndGebExcel("./gen_src_code", PromptConstants.PROMPT);

        ExcelRecord excelRecord = new ExcelRecord();
        String id = IdUtil.fastUUID();
        excelRecord.setId(id);
        excelRecord.setCompliedClassPath("./gen_src_code/"+PromptConstants.PROMPT);
        excelRecord.setCreateTimestamp(new Date());
        excelRecordMapper.insert(excelRecord);


        return SubmitRespones.builder()
                .message(text)
                .template_id(id)
                .excelResponse(excelResponse)
                .build();
    }
    public String loadClassAndGebExcel(String classPath,String filePath) throws Exception {
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(classPath);

            logger.info("Class path inserted: {}", classPath);
            // 使用新的类加载器加载类
            CtClass ctClass = pool.get(filePath);
            ClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{new java.io.File(classPath).toURI().toURL()});
            Class<?> loadedClass = ctClass.toClass(classLoader, null);

            Method method = loadedClass.getMethod("main", String[].class);
            String[] mainArgs = new String[]{};
            method.invoke(null, (Object) mainArgs);
            return "Class loaded and executed successfully";

        } catch (Exception e) {
            logger.error("Error loading class and generating Excel: {}", e);
            return "Error loading class and generating Excel: " + e.getMessage();
        }

    }

    private GenerationResult generateCode(String markdown, String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        com.alibaba.dashscope.utils.Constants.apiKey = CredentialConstants.APIKEY;

        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(PromptConstants.SYSTEM_PROMPT)
                .build();

        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(markdown + message + PromptConstants.getUserPrompt())
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(com.alibaba.dashscope.utils.Constants.apiKey)
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        return gen.call(param);
    }

    private String updateExcelName() {
        LambdaQueryWrapper<ExcelRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ExcelRecord::getCreateTimestamp);
        List<ExcelRecord> record = excelRecordMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(record)){
            return "ExcelModifier1";
        }
        String fileName =  record.get(0).getCompliedClassPath().substring(record.get(0).getCompliedClassPath().lastIndexOf("E"));
        String regex = "(\\D*)(\\d+)$"; // 匹配结尾的数字部分，\\D*表示非数字部分，\\d+表示数字部分
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            // 获取非数字部分
            String prefix = matcher.group(1);
            // 获取数字部分并递增
            int number = Integer.parseInt(matcher.group(2)) + 1;
            // 返回递增后的字符串
            return prefix + number;
        } else {
            // 如果没有找到数字，直接返回原始字符串
            return fileName;
        }
    }

    private String saveJavaCodeToFile(String javaCode, String path, String fileName) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + path);
            }
        }
        // 拼接文件路径
        String filePath = path + fileName;
        // 写入 Java 代码到指定文件
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(javaCode);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Java code to file: " + filePath, e);
        }
        // 返回保存的文件路径
        return filePath;
    }
    /**
     * selectTemplate
     * @param request
     * @return
     */
    @Override
    public List<ExcelRecord> selectTemplate(ExcelRecord request) {
        if(request == null || StringUtils.isBlank(request.getStatus())){
            return null;
        }
        QueryWrapper<ExcelRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("status", request.getStatus());
        List<ExcelRecord> list = excelRecordMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
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


    /**
     * generate an excel and return the path
     *
     * @param request
     * @return
     */
    @Override
    public String reGen(FileUploadRequest request) throws Exception {
        String templateId = request.getTemplate_id();
        ExcelRecord excelRecord = excelRecordMapper.selectByPrimaryKey(templateId);

        String compliedClassPath = excelRecord.getCompliedClassPath();

        int slashIndex = compliedClassPath.lastIndexOf("/");
        String classPath = compliedClassPath.substring(0, slashIndex);
        String className = compliedClassPath.substring(slashIndex + 1);

        loadClassAndGebExcel(classPath, className);

        return "./excel_file/output.xlsx";


    }
}