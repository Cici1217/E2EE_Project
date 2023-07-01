package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class testController {
    @RequestMapping(value = "/TE")
    @ResponseBody
    public String t(){
        return "test";
    }
}
