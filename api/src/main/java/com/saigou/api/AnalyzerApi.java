package com.saigou.api;

import com.saigou.vo.StreamProcessorVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "analyzer-server-10001")
public interface AnalyzerApi {

    @GetMapping("/analyzer/get/{id}")
    StreamProcessorVO getById(@PathVariable("id") Long id);

    @GetMapping("/analyzer/list")
    List<StreamProcessorVO> getAll();
    
    @GetMapping("/analyzer/execute/{id}")
    void execute(@PathVariable("id") Long id);

    @GetMapping("/analyzer/cancel/{id}")
    void cancel(@PathVariable("id") Long id);

    @GetMapping("/analyzer/create")
    StreamProcessorVO create(@RequestParam("id") Long id, @RequestParam("url") String url, @RequestParam("stream") String stream);

    @GetMapping("/analyzer/remove/{id}")
    void remove(@PathVariable("id") Long id);

}
