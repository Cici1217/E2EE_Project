package demo.Mapper;

import demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User getById(int id);
    User getByUsername(String username);
    int selectMaxId();
    void insertOneUser(int id,String username,String password,String registrationID);
    String getPasswordByUsername(String username);

    int getRidByUserid(String userId);
}
