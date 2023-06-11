package demo.controller;

import com.alibaba.fastjson.JSON;
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
        System.out.println("group or id");
        int groupId = map.get("groupId");
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
        List<Integer> userIds=new ArrayList<>();
        if(groupId!=-1) userIds = groupMapper.selectByGroupId(groupId);
        else userIds.add(map.get("userId"));
//        //这个地方取到了真正的userid
//        System.out.println(userIds);
//        System.out.println(groupId);
        Map<Integer, Integer> maps = new HashMap<>();
        //userid -> rid
        for (Integer userid : userIds) {
//            String username = userMapper.getById(userid).getUsername();
            maps.put(userid, userMapper.getRidByUserid(String.valueOf(userid)));
        }
        System.out.println("maps here:");
        System.out.println(maps);
        //username -> rid Map
        return maps;
    }


    @RequestMapping("/createGroup")
    @ResponseBody
    public boolean createGroup(
            @RequestBody Map<String, List<Integer>> map) {
        List<Integer> list = map.get("groupMember");
        System.out.println(list);
        GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
        int groupId = list.get(list.size()-1);
        for (int i = 0; i < list.size()-1; i++) {
            int userId = list.get(i);
            groupMapper.addGroup(groupMapper.selectMaxId()+1,groupId,userId);
            sqlSession.commit();
        }
        return true;
    }
}
