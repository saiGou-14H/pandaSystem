package com.saigou.api;

import com.saigou.entity.Scourse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "course-provider-9004")
public interface CourseApi {

    @GetMapping("/course/get/{id}")
    Scourse getById(@PathVariable("id") Long id);

    @PostMapping("/course/add")
    int add(@RequestBody Scourse scourse);

    @DeleteMapping("/course/delete/{id}")
    int delete(@PathVariable("id") Long id);

    @PutMapping("/course/update")
    int update(@RequestBody Scourse scourse);

    @GetMapping("/course/all")
    List<Scourse> getAll();
}
