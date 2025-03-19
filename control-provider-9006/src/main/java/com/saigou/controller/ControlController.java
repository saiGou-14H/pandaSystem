package com.saigou.controller;

import com.saigou.entity.Control;
import com.saigou.service.IControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
public class ControlController {
    private final IControlService iControlService;


    @PostMapping("/add")
    public int add(@RequestBody Control control){
        return iControlService.add(control);
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable("id") Long id){
        return iControlService.delete(id);
    }

    @PutMapping("/update")
    public int update(@RequestBody Control control){
        return iControlService.update(control);
    }

    @GetMapping("/getById/{id}")
    public Control getById(@PathVariable("id") Long id){
        return iControlService.getById(id);
    }

    @GetMapping("/all")
    public List<Control> getAll(){
        return iControlService.getAll();
    }

}
