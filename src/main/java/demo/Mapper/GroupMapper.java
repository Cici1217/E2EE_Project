package demo.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<Integer> selectByGroupId(int groupId);
    void setGroup(int id,int groupId,int userId);
    int selectMaxId();
}
