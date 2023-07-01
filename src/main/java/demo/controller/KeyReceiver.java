package demo.controller;


import demo.Mapper.KeyBundleMapper;
import demo.Mapper.PreKeyMapper;
import demo.Mapper.SignedPreKeyMapper;
import demo.Mapper.UserMapper;
//import demo.config.WebSocket;
import demo.pojo.*;
import demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static demo.OpenMybatis.sqlSession;

@Controller
@ResponseBody
public class KeyReceiver {

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

    @GetMapping(value = "/keyOf/{uid}")
    @ResponseBody
    public InitialKeyBundle key(@PathVariable String uid) {
        //uid:username
        System.out.println("keysL="+uid);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User tempUser = userMapper.getByUsername(uid);
        int userPrimaryKey = tempUser.getId();
        String rId = tempUser.getRegistrationID();
        KeyBundleMapper keyBundleMapper = sqlSession.getMapper(KeyBundleMapper.class);
        PreKeyMapper preKeyMapper = sqlSession.getMapper(PreKeyMapper.class);
        SignedPreKeyMapper signedPreKeyMapper = sqlSession.getMapper(SignedPreKeyMapper.class);
        String identityKey = keyBundleMapper.selectIdentityKey(userPrimaryKey);

        List<Integer> keyId = preKeyMapper.selectKeyIdByUserId(userPrimaryKey);
        List<String> publicKey = preKeyMapper.selectPublicKeyByUserId(userPrimaryKey);

        System.out.println("identityKey:"+identityKey);
        InitialKeyBundle i = new InitialKeyBundle(uid,rId,identityKey,
                new SignedPreKey(signedPreKeyMapper.selectKeyIdByUserId(userPrimaryKey),
                        signedPreKeyMapper.selectPublicKeyByUserId(userPrimaryKey),
                        signedPreKeyMapper.selectSignatureByUserId(userPrimaryKey)
                )
                ,new PreKey(keyId.get(0),publicKey.get(0)));
        System.out.println(i);

//        public InitialKeyBundle initialKeyBundle(){
//            return new InitialKeyBundle(userId, registrationId, identityKey, signedPreKey, preKeys.pop());
//        }

//        return i;
//        InitialKeyBundle i2 = WebSocket.getKeyBy(uid).initialKeyBundle();
//        System.out.println(i2);
        System.out.println("---------------------------------------------------------");
        return i;
    }
}
