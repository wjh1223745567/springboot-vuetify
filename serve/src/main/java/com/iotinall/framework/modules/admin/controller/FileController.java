package com.iotinall.framework.modules.admin.controller;

import com.iotinall.framework.util.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "tmpfile")
public class FileController {

    @GetMapping(value = "/{name}")
    public void loadFile(@PathVariable(value = "name") String name, HttpServletResponse response){
        FileUtil.loadFile(name, response);
    }

}
