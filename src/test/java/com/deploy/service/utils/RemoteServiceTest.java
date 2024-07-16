package com.deploy.service.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RemoteServiceTest {

    @Autowired
    RemoteService remoteService;

    String host, username, pemContent = null;
    int port;


    @BeforeEach
    public void init() {
        this.host = "52.21.5.88";
        this.port = 22;
        this.username = "ec2-user";
        this.pemContent = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEA61lhUBHpDwJ2FGzglmNpqHvOXdCi6DuHhT+rIvoe93P9c5MS\n" +
                "YAwCKcGYcQ3d9lFXDlL1B4YLSZlUJHW73NTEHRIVBRWi+EXwLehN9W3fR/mC/7X9\n" +
                "xiRCXn1qXTKpndVCUEKbQKHk1PDivqQxcyPRfB1si8cYIPCZkGGlo+GazCOmt6Gm\n" +
                "9VZpVgCAlTiKOiFF7Qdm6LrRCaI4TtbZIyM09HExCDnxp7OagK2RWPsPM+fnG3i0\n" +
                "GH25wxreh+0bAKwM7LEqi+hVTKFaFn9rLVgJWpy43ASpnPH8Tl+X44PTsbPYRaKn\n" +
                "RYVCQtN0FAqzTBnZqRqwW3QtjUtAfry5qLhwZwIDAQABAoIBAQCVxvAzcPKNZteW\n" +
                "8COOEf5wBzqyYdELUrdQidB0FhIXEW4/W13aWkoWIOrPKDAWTnXE2+6stxX/5OCz\n" +
                "w2mnhJC6n4NZFQf+USQlUy4p/56Vw6km679xlinW0KIcZd2kYNAvG/SKEX38NsFW\n" +
                "6k++IxFyl5c47Z/hdr2EMWClBzLorp9B4mmsH7rZ5+UTU9hU/nLsZ/F7wTknIQj7\n" +
                "8NvWEnW8RLlIW5jRiovSEH9EaGMK/B5Bw3qm+DWlxXNCJ4o9YwZoh0iNCJmlwnMf\n" +
                "Sf2GWaxtmnHJqastalV54nCxHf7b+Yzo8jYaNee9MvQ6jlqQdmie7/PKuohf2P/p\n" +
                "6GecREHhAoGBAPZpJT8wr+k1kIDGpyMo3qzkKQyNlzZW0sh8ZP1nCBo+Ch/QDBxM\n" +
                "T3E6+nbijM9WLHpd9K940N7YWUUq2J3/q1ugTWeBO+iEWUcjzKIth4hVEODjOBTq\n" +
                "SBKdNjRroAfV03dn1wmqcsRhgL6o59p5eO6fg+RwG14PgmhWuJsmYYoFAoGBAPSC\n" +
                "CL7JxeiI6ZXSfZdt08v2N12ZQz7sGbRwGqb62IV+0790sYVvNVU3SS3tMGhB6HHi\n" +
                "dIxH9Yx8pczgGFDhAQ/mDE1jZWe10W9f0tg4HcaIJLS3DZztpuyjC4SXWTobtU8y\n" +
                "zG6DWvTKDl9nokmlFE3fxiobhwv41DaWsWU2uKB7AoGBANgf/SVhhMldy/LHSo2S\n" +
                "KU0nicGS5xAoMxTZ1pJULk0mIScqCZVAcWV1P33K04p/oN9rTVQi+cCbriD2paxf\n" +
                "NWNWRM4GEg+tFclJ4xBUMs4nHnjBksz1eGWrMoHj7CczKxlOINQ/hg4tHwkbiNCd\n" +
                "Pq69hqd0lOx5Wf4+IgkjLuYpAoGATjaaXY1lxXCmZ8qhaiMzsPbd1w8Dt8kGn/WM\n" +
                "UQXE5U8gpQnLD4f3Y37/5bUN2wvaMzPhXE5YecwVrWex341aLZ/FJ6w37+j1Sc85\n" +
                "PvkUbUF3nGdB74UF9IRjVtKjNDdQ1DjHtEJIgi1wU4xvGWe5CwAd/7I2jNnX6G5j\n" +
                "6KCMhqkCgYAVpuFxxeb75i1GnKZWbaR2rgcLUVWz/XevCHMJg3d47SSpQTdFwlYL\n" +
                "K2b9LbukPpxG/qWvbfwjuxI3EHGViezlZ/uUlQLX1mQVUb+B9ZTyEy6UgxgjMPzw\n" +
                "8HzWOXClz68jg8tRxvxOqNmI+n4LLQFlE6BgbUPvrT1f1AZ9vJp5+g==\n" +
                "-----END RSA PRIVATE KEY-----";
    }


    @Test
    public void 스크립트실행_테스트(){

        String script = "java -jar new-deploy-app.jar";

        try {
            remoteService.init(host, port, username, null, pemContent);
            String s = remoteService.executeScript(script);
            System.out.println("s = " + s);

        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
//            e.printStackTrace();
        }

    }


    @Test
    public void 업로드_테스트() {

        try {

            File uploadFile = new File("/Users/hwaneehwanee/dev/project/deploy-app/build/libs/deploy-app.jar");
            String uploadPath = "/home/ec2-user";
            String newFileName = "new-deploy-app.jar";

            remoteService.init(host, port, username, null, pemContent);
            boolean upload = remoteService.upload(uploadFile, uploadPath, newFileName);


            assertThat(upload).isTrue();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}