package demo.controller;


import demo.Mapper.KeyBundleMapper;
import demo.Mapper.PreKeyMapper;
import demo.Mapper.SignedPreKeyMapper;
import demo.Mapper.UserMapper;
import demo.pojo.User;
import demo.pojo.InitialKeyBundle;
import demo.pojo.PreKey;
import demo.pojo.SignedPreKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static demo.OpenMybatis.sqlSession;

@Controller
public class KeyGetter {
    @CrossOrigin
    @GetMapping(value = "/keyOf/{uid}")
    @ResponseBody
    public InitialKeyBundle key(@PathVariable String uid) {
        //uid:username
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
