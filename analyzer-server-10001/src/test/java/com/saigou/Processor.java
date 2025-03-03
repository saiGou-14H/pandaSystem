package com.saigou;

import com.saigou.draw.Draw;
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
    static {
        // 启用FFmpeg详细日志
        FFmpegLogCallback.set();
    }
    @Autowired
    Draw draw;
    @Test
    public void test() throws InterruptedException {
        draw.run1();
    }

    public static void main(String[] args) throws InterruptedException {
        String url1 = "rtsp://127.0.0.1:7554/live/30";
        String url2 = "rtmp://127.0.0.1:7935/live/test3";
        videoStreamProcessor videoStreamProcessor = new videoStreamProcessor(url1, url2);
        videoStreamProcessor.start();
        Thread.sleep(100000000);
        videoStreamProcessor.stop();
    }
}
