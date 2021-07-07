package com.todoapp.controller;

import com.todoapp.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    TaskConfigurationProperties myProp;
    DataSourceProperties datasource;

    public InfoController(TaskConfigurationProperties myProp, DataSourceProperties datasource) {
        this.myProp = myProp;
        this.datasource = datasource;
    }

    @GetMapping("/myprop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }

    @GetMapping("/url")
    String sourceUrl(){
        return datasource.getUrl();
    }
}
