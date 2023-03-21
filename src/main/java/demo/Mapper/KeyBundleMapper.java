package demo.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeyBundleMapper {
    void setKeyBundle(int id, int userId, String identityKey);
    int selectMaxId();
    String selectIdentityKey(int userId);
}
