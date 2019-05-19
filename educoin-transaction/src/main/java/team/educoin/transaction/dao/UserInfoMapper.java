package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.UserInfo;

import java.util.List;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@Component
public interface UserInfoMapper {

    List<UserInfo> selectAllUser();

    @Select({"select email, account_balance from user_info where email=#{email}"})
    UserInfo selectRecordById(@Param("email") String email);

    @Update({"update user_info set bankAccount=#{amount} where email=#{id}"})
    int updateBankAccountById(String id, String amount);
}
