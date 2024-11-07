package com.hkct.aiexcel.Utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import com.hkct.aiexcel.constants.CredentialConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class CommonOssUtils {

    public static CredentialsProvider credentialsProvider = new DefaultCredentialProvider(CredentialConstants.ACCESS_KEY_ID, CredentialConstants.ACCESS_KEY_SECRET);

    public static OSS ossClient = new OSSClientBuilder().build(CredentialConstants.END_POINT, credentialsProvider);

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
                ossClient.shutdown();
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
            ossClient.shutdown();
        }
    }

    /**
     * downloadFileFromOss
     */
    public static void downloadFile() {
        // write bucketName
        String bucketName = "java-hello-world";
        // 填写Object完整路径，需要包含文件名，但不用包含Bucket名称
        String objectName = "test/test.txt";

        try {
            // Calling ossClient.getObject returns an OSSObject instance, which contains the file content and file metadata.
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);

            // The file input stream can be obtained by calling ossObject.getObjectContent, and its content can be read from this input stream.
            InputStream content = ossObject.getObjectContent();

            if (content != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    log.info("\n" + line);
                }
                // close
                content.close();
            }
        } catch (Exception e) {
            log.error("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
