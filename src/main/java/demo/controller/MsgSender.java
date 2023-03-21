package demo.controller;


import demo.pojo.Msg;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgSender {
    @CrossOrigin
    @GetMapping(value = "/chat")
    public Msg send() {
        System.out.println("Here");
        Msg res = new Msg();
        res.setMsg("buhao");
        res.setToID("12011001");
        res.setToRegistrationID("233");
        return res;
    }
}
