package com.saigou;

import cn.hutool.json.JSONObject;
import com.saigou.entity.StreamProcessor;
import com.saigou.util.KafkaSendService;
import com.saigou.util.KafkaSinkHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)  // 使用 Spring 测试运行器
@SpringBootTest  // 加载 Spring Boot 上下文
@Service
public class Processor {
    @Autowired
    KafkaSendService kafkaSendService;
    @Autowired
    KafkaSinkHandler kafkaSinkHandler;
    @Test
    public void test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("test", "test");
//        kafkaSinkHandler.receive(jsonObject);
        kafkaSendService.send(jsonObject);

    }
}
