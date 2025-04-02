package com.saigou.Controller;

import com.saigou.api.CourseApi;
import com.saigou.entity.Scourse;
import com.saigou.util.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("course")
public class CourseController {
    @Autowired
    CourseApi courseApi;

    @PostMapping("add")
    @Operation(summary = "新增",description = "新增课程")
    public ResponseVO add(Scourse course)
    {
        return ResponseVO.success(courseApi.add(course));
    }
    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除",description = "删除课程")
    public ResponseVO delete(@PathVariable Long id)
    {
        return ResponseVO.success(courseApi.delete(id));
    }

    @PutMapping("update")
    @Operation(summary = "修改",description = "修改课程信息")
    public ResponseVO update(Scourse course)
    {
        return ResponseVO.success(courseApi.update(course));
    }

    @GetMapping("get/{id}")
    @Operation(summary = "查询",description = "根据id查找课程")
    public ResponseVO getById(@PathVariable Long id)
    {
        return ResponseVO.success(courseApi.getById(id));
    }

    @GetMapping("all")
    @Operation(summary = "查询",description = "查询所有课程")
    public ResponseVO getAll()
    {
        return ResponseVO.success(courseApi.getAll());
    }
    @GetMapping("list")
    @Operation(summary = "查询",description = "查询班级所有课程")
    public ResponseVO getListByStudent() {
        return ResponseVO.success(courseApi.getAll());
    }

}
