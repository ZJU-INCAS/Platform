package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.UserInfo;

/**
 * @description: admin_info
 * @author: PandaClark
 * @create: 2019-05-16
 */
@Component
public interface AdminInfoMapper {

    @Select({"select email, account_balance from admin_info where email=#{email}"})
    UserInfo selectRecordById(String email);
}
