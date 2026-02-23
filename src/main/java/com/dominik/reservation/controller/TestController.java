package com.dominik.reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String root() {
        return "Reservation system is running";
    }

    @GetMapping("/api/test")
    public String test() {
        return "OK";
    }
}