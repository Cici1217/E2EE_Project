package demo.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<Integer> selectByGroupId(int groupId);

    int selectMaxId();

    void addGroup(@Param("id") int id, @Param("groupid") int groupid, @Param("userid") int userid);
}
