package demo.controller;


import demo.Mapper.KeyBundleMapper;
import demo.Mapper.PreKeyMapper;
import demo.Mapper.SignedPreKeyMapper;
import demo.Mapper.UserMapper;
//import demo.config.WebSocket;
import demo.pojo.KeyBundle;
import demo.pojo.PreKey;
import demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

import static demo.OpenMybatis.sqlSession;

@Controller
public class KeyReceiver {
    @CrossOrigin
    @PostMapping(value = "/keysOf/{uid}")
    @ResponseBody
    public Result receiveKey(@RequestBody KeyBundle keyBundle, @PathVariable String uid) {
        System.out.println("Receive Key");
        System.out.println(keyBundle);
        System.out.println(keyBundle.getPreKeys().get(1));
        System.out.println(keyBundle.getIdentityKey());
//        WebSocket.setKeyMap(uid, keyBundle);

        //放到数据库里
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int userPrimaryKey = userMapper.getByUsername(keyBundle.getUserId()).getId();
        KeyBundleMapper keyBundleMapper = sqlSession.getMapper(KeyBundleMapper.class);
        PreKeyMapper preKeyMapper = sqlSession.getMapper(PreKeyMapper.class);
        SignedPreKeyMapper signedPreKeyMapper = sqlSession.getMapper(SignedPreKeyMapper.class);
        LinkedList<PreKey> preKeys = keyBundle.getPreKeys();
        for (PreKey preKey : preKeys) {
            preKeyMapper.setPreKey(preKeyMapper.selectMaxId() + 1, userPrimaryKey, preKey.getKeyId(), preKey.getPublicKey());
        }
        keyBundleMapper.setKeyBundle(keyBundleMapper.selectMaxId() + 1, userPrimaryKey, keyBundle.getIdentityKey());
        signedPreKeyMapper.setSignedPreKey(signedPreKeyMapper.selectMaxId() + 1, userPrimaryKey,
                keyBundle.getSignedPreKey().getKeyId(), keyBundle.getSignedPreKey().getPublicKey(), keyBundle.getSignedPreKey().getSignature()
        );
        sqlSession.commit();

        return new Result(200);
    }
}
