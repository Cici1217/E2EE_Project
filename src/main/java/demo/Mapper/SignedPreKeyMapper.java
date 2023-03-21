package demo.Mapper;

import demo.pojo.SignedPreKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignedPreKeyMapper {
    void setSignedPreKey(int id, int userId, int keyId, String publicKey, String signature);

    int selectMaxId();
    int selectKeyIdByUserId(int userId);
    String selectPublicKeyByUserId(int userId);
    String selectSignatureByUserId(int userId);
    SignedPreKey selectByUserId(int userId);
}
