package com.atibusinessgroup.amanyaman.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/app")
public class HelloController {
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String hello() {
        return "hello";
    }
}
