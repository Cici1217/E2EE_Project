package demo.controller;


import demo.pojo.Msg;
import demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MsgReceiver {
    @CrossOrigin
    @PostMapping(value = "/chat")
    @ResponseBody
    public Result receive(@RequestBody Msg msg){
        String receivedMsg = msg.getMsg();
        receivedMsg = HtmlUtils.htmlEscape(receivedMsg);
        System.out.println(receivedMsg);
        System.out.println(msg.getToID());
        System.out.println(msg.getToRegistrationID());
        return new Result(200);
    }
}
