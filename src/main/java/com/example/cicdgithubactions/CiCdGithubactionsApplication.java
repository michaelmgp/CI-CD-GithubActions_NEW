package com.example.cicdgithubactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CiCdGithubactionsApplication {

    @GetMapping("/Welcome")
    public String welcome(){
        return "Welcome to Michael Page";
    }

    public static void main(String[] args) {
        SpringApplication.run(CiCdGithubactionsApplication.class, args);
    }

}
