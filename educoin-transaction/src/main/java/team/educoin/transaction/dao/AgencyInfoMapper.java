package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.UserInfo;

/**
 * @description: agency_info
 * @author: PandaClark
 * @create: 2019-05-16
 */
@Component
public interface AgencyInfoMapper {

    @Select({"select email, account_balance from agency_info where email=#{email}"})
    UserInfo selectRecordById(String email);

    @Update({"update agency_info set accountBalance=#{amount} where email=#{id}"})
    int updateBankAccountById(String id, String amount);
}
