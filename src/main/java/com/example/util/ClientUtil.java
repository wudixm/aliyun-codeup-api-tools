package com.example.util;

import com.aliyun.devops20210625.Client;
import com.aliyun.teaopenapi.models.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClientUtil {
    public static final String ORGANIZATION_ID;
    private static final String accessKeyId;
    private static final String accessSecret;
    private static final String endpoint;
    private static Client client;

    // accessKeyId=LTI5tS...
    //accessSecret=ETwnY4N...
    //endpoint=devops.cn-hangzhou.aliyuncs.com  Endpoint 请参考 https://api.aliyun.com/product/devops
    //ORGANIZATION_ID=5f0...

    static {
        try {
            File file = new File("/Users/Shared/config.properties");
            if (file.exists()) {
                java.util.Properties properties = new java.util.Properties();
                properties.load(new java.io.FileInputStream(file));
                accessSecret = properties.getProperty("accessSecret");
                accessKeyId = properties.getProperty("accessKeyId");
                endpoint = properties.getProperty("endpoint");
                ORGANIZATION_ID = properties.getProperty("ORGANIZATION_ID");
            } else {
                throw new FileNotFoundException("config.properties not found");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientUtil() {
    }

    public static Client getClient() throws Exception {
        if (client == null) {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessSecret);
            // Endpoint 请参考 https://api.aliyun.com/product/devops
            config.endpoint = endpoint;
            client = new Client(config);
        }
        return client;
    }
}
