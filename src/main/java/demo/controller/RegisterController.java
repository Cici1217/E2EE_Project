package demo.controller;

import demo.Mapper.UserMapper;
import demo.pojo.User;
import demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static demo.OpenMybatis.sqlSession;

@Controller
@ResponseBody
public class RegisterController {
    public RegisterController() throws IOException {
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    public Result register(
            @RequestBody Map<String, String> map
    ) {
        User temp = sqlSession.getMapper(UserMapper.class).getByUsername(map.get("username"));
        System.out.println("here::::::::::" + temp);
        if (temp != null) {
            System.out.println("exist!");
            return new Result(404);
        }
        try {
            Integer.valueOf(map.get("username"));
        } catch (Exception e) {
            return new Result(406);
        }
        if (map.get("username").equals(""))
            return new Result(406);

        return new Result(200);
    }

    @RequestMapping(value = "/register1")
    @ResponseBody
    public Result register1(
            @RequestBody User requestUser
    ) {
        System.out.println("register1:_______________" + requestUser);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        String username = requestUser.getUsername();
        String pwd = requestUser.getPassword();
        String rid = requestUser.getRegistrationID();
        int id = userMapper.selectMaxId() + 1;
        userMapper.insertOneUser(id, username, pwd, rid);
        sqlSession.commit();
        return new Result(200);
    }
//
//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @PostMapping("/redis/{k}/{v}")
//    public String addStringKV(@PathVariable String k,
//                              @PathVariable String v) {
//
//        // 使用StringRedisTemplate对象
//        redisTemplate.opsForValue().set(k, v);
//        return "使用StringRedisTemplate对象添加";
//    }
//
//
//    public String getData(String key) {
//        Object v = redisTemplate.opsForValue().get(key);
//        return "key是" + key + ",它的值是:" + v;
//    }
//
//    @RequestMapping("/checkToken")
//    public boolean check() {
//        String token = request.getHeader("token");
//        return JWTUtils.isExpire(token);
//    }
}