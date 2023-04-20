package demo.controller;

import demo.Mapper.GroupMapper;
import demo.Mapper.UserMapper;
import demo.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static demo.OpenMybatis.sqlSession;

@Controller
public class GroupOrIndividualController {
    @RequestMapping("/groupOrIndividual")
    @ResponseBody
//    id -> rid
    public Map<Integer, Integer> goi(
            @RequestBody Map<String, Integer> map
    ) {
        int groupId = map.get("groupId");
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        int userId = map.get("userId");
        if (groupId == -1) {
            String username = userMapper.getById(userId).getUsername();
            Map<Integer, Integer> map1 = new HashMap<>();
            map1.put(Integer.valueOf(username), userMapper.getRidByUserid(userId));
            return map1;
        }

        GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
        List<Integer> userIds = groupMapper.selectByGroupId(groupId);

        Map<Integer, Integer> maps = new HashMap<>();
        //userid -> rid
        for (Integer userid : userIds) {
            String username = userMapper.getById(userid).getUsername();
            maps.put(Integer.valueOf(username), userMapper.getRidByUserid(userid));
        }
        System.out.println("maps here:");
        System.out.println(maps);
        return maps;
    }
}
