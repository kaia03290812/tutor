package com.gtalent.tutor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolloController {

    @GetMapping("/hello")
    public String hello()
    {
return  "Hello,World";
    }

    @GetMapping("/happy")
    public String happy()
    {
        return  "HappyDay";
    }
}
