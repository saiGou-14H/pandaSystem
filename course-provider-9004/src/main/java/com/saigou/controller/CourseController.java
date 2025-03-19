package com.saigou.controller;

import com.saigou.entity.Scourse;
import com.saigou.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("course")
public class CourseController {
    private final ICourseService service;
    @PostMapping("add")
    @Operation(summary = "新增",description = "新增课程")
    public int add(Scourse course) {
        return service.add(course);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除",description = "删除课程")
    public int delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改课程信息")
    public int update(Scourse course) {
        return service.update(course);
    }

    @GetMapping("get/{id}")
    @Operation(summary = "查询",description = "根据id查找课程")
    public Scourse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("all")
    @Operation(summary = "查询",description = "查询所有课程")
    public List<Scourse> getAll() {
        return service.getAll();
    }


}
