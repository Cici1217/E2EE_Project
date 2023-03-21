package demo.Mapper;

import demo.pojo.PreKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PreKeyMapper {
    void setPreKey(int id,int userId,int keyId,String publicKey);
    int selectMaxId();
    List<Integer> selectKeyIdByUserId(int userId);
    List<String> selectPublicKeyByUserId(int userId);

    List<PreKey> selectAll();
}
