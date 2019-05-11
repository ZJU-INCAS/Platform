package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.AgencyInfo;
import team.educoin.transaction.pojo.UserInfo;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user_info")
    List<UserInfo> selectAllUser();

    // 根据用户email获取用户信息
    @Select("select * from person_info where email = #{email}")
    List<UserInfo> queryUserById(@Param("email") String email);

    // 根据机构email获取机构信息
    @Select("select * from agency_info where email = #{email}")
    List<AgencyInfo> queryAgencyById(@Param("email") String email);
}
