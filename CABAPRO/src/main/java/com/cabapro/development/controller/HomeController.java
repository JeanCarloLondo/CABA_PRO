/**
 *  Controller for managing home page and login page.
 *
 * Author: Alejandra Ortiz
 * Date: 2025-09-04
 * Role: Controller -  Home
 */
package com.cabapro.development.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //home page
    @GetMapping("/")
    public String index() {
        return "home/index"; // resources/templates/home/index.html
    }

    //login page
    @GetMapping("/login")
    public String login() {
        return "home/login";// resources/templates/home/login.html
    }
}
