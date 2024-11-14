package com.hkct.aiexcel.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import com.hkct.aiexcel.constants.CredentialConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;

@Slf4j
public class CommonOssUtils {

    private static OSS ossClient;

    // Initialize the OSS client
    public static void initializeClient() {
        if (ossClient == null) {
            ossClient = new OSSClientBuilder().build(CredentialConstants.END_POINT, CredentialConstants.ACCESS_KEY_ID,  CredentialConstants.ACCESS_KEY_SECRET);
        }
    }

    // Shutdown the OSS client
    public static void shutdownClient() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    // 使用阿里云的AccessKey ID 和 AccessKey Secret 创建凭证提供器
//    public static CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
//            CredentialConstants.ACCESS_KEY_ID,
//            CredentialConstants.ACCESS_KEY_SECRET
//    );

    // 创建 OSS 客户端对象
//    public static OSS ossClient = new OSSClientBuilder().build(
//            CredentialConstants.END_POINT,
//            credentialsProvider
//    );


    public static String uploadFileFromLocal(String localFilePath,String objectName) {

        initializeClient();
        // 创建文件对象
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在: " + localFilePath);
        }
        String bucketName = "ai-excel";

        // 上传文件到 OSS
        ossClient.putObject(new PutObjectRequest(bucketName, objectName, file));
        // 生成文件的访问 URL
        String fileUrl = "https://" + bucketName + "." + CredentialConstants.END_POINT + "/" + objectName;
        return fileUrl;
    }


    /**
     * if you need create a new bucket, you can use this function
     */
    public static void createBucket() {
        String bucketName = "ai-excel";
        try {
            // create bucket
            ossClient.createBucket(bucketName);
        } catch (Exception e) {
            log.error("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
//                ossClient.shutdown();
            }
        }
    }

    /**
     * saveJavaCodeToOss
     *
     * @param javaCode
     * @param objectName
     */
    public static void saveJavaCodeToOss(String javaCode, String objectName) {
        initializeClient();
        // can change this bucketName
        String bucketName = "ai-excel";
        // create OSS client
        try {
            // javacode to stream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(javaCode.getBytes());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);

            // upload to OSS
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("javaCode successfully uploaded to OSS, target {} ", result.getETag());
        } catch (Exception e) {
            log.error("failed to upload to OSS,{}", e.getMessage());
            e.printStackTrace();
        } finally {
            // Close OSS client
//            ossClient.shutdown();
        }
    }

    /**
     * downloadFileFromOss
     */
    public static String downloadFile(String objectName) {
        // Bucket 名称
        initializeClient();
        String bucketName = "ai-excel";

        // 生成预签名 URL
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
        request.setExpiration(java.util.Date.from(java.time.Instant.now().plusSeconds(3600)));

        // 获取生成的 URL
        URL url = ossClient.generatePresignedUrl(request);
        System.out.println(url);
        return url.toString();
    }
}
