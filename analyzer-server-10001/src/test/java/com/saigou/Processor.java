package com.saigou;

import com.saigou.util.StreamProcessor;
import org.bytedeco.javacv.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)  // 使用 Spring 测试运行器
@SpringBootTest  // 加载 Spring Boot 上下文
@Service
public class Processor {
//    static {
//        // 启用FFmpeg详细日志
//        FFmpegLogCallback.set();
//    }
    @Autowired
    StreamProcessor streamProcessor;

    @Autowired
    StreamProcessor streamProcessor2;

    @Test
    public  void testa(){
        String url1 = "rtsp://127.0.0.1:7554/live/30-2";
        String url2 = "rtmp://127.0.0.1:7935/live/test3";

        String url3 = "rtsp://127.0.0.1:7554/live/30";
        String url4 = "rtmp://127.0.0.1:7935/live/test4";
        streamProcessor.init(url1,url2);
        streamProcessor.start();
        streamProcessor2.init(url3,url4);
        streamProcessor2.start();
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        streamProcessor.stop();
    }
}
