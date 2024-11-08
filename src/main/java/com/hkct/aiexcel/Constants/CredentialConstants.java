package com.hkct.aiexcel.constants;

public class CredentialConstants {
    //这里填写自己申请的APIKEY..
    public static final String APIKEY = System.getenv("DASHSCOPE_API_KEY");
    public static final String ACCESS_KEY_ID = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
    public static final String ACCESS_KEY_SECRET = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
    public static final String END_POINT = "oss-cn-shenzhen.aliyuncs.com";

}

//配置到环境变量的方法
//https://help.aliyun.com/zh/direct-mail/configure-the-authentication-accesskey-in-the-environment-variable
//https://help.aliyun.com/zh/model-studio/developer-reference/configure-api-key-through-environment-variables?spm=a2c4g.11186623.0.0.61fc2066UoMrWw#e4cd73d544i3r