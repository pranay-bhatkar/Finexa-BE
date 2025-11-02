package com.expense_tracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static  final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/")
    public String home() {

        logger.info("Received request at '/' endpoint");
        logger.debug("⚙️ Executing HelloController.home() method");

        return "Expense Tracker Backend is running successfully";
    }
}