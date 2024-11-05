package com.hkct.aiexcel.Config;

import com.aliyun.docmind_api20220711.Client;
import com.aliyun.teaopenapi.models.Config;
import com.hkct.aiexcel.Constants.CredentialConstants;

public class ClientConfig {

    public static Client createClient() throws Exception {
        // 创建Client实例并设置配置
        Config config = new Config()
                .setAccessKeyId(CredentialConstants.ACCESS_KEY_ID)
                // 通过credentials获取配置中的AccessKey Secret
                .setAccessKeySecret(CredentialConstants.ACCESS_KEY_SECRET);

        //访问的域名，支持ipv4和ipv6两种方式，ipv6请使用docmind-api-dualstack.cn-hangzhou.aliyuncs.com
        config.endpoint = "docmind-api.cn-hangzhou.aliyuncs.com";
        return new Client(config);
    }
}
