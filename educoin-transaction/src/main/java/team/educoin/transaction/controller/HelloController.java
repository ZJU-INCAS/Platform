package team.educoin.transaction.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/")
    @ApiOperation(value = "hello world!", notes = "say hello springboot")
    public String hello(Model m) {
        m.addAttribute("name", "thymeleaf");
        return "hello";
    }
}
