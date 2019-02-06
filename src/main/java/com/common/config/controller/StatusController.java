package com.common.config.controller;

import com.common.web.AbstractController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class StatusController extends AbstractController {
    @RequestMapping("status")
    public Map<String, Object> status() {
        return buildMessage(() -> {
            return null;
        });
    }
}
