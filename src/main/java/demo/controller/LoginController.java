package demo.controller;

import demo.Mapper.UserMapper;
import demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static demo.OpenMybatis.sqlSession;

@Controller
@ResponseBody
public class LoginController {

    @RequestMapping("/login")
    @ResponseBody
    public Result login(
            @RequestBody Map<String, String> map
    ) {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String pwd = userMapper.getPasswordByUsername(map.get("username"));
        System.out.println(map.get("password"));
        System.out.println(pwd);
        boolean judge = pwd.equals(map.get("password"));
        System.out.println(judge);
        if (judge) {
            return new Result(200);
        }
        return null;
    }
}
