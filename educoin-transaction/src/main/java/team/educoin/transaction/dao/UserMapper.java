package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.UserInfo;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user_info")
    List<UserInfo> selectAllUser();
}
