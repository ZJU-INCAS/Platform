package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Insert;
import team.educoin.transaction.pojo.Token;

/**
 * @description: 操作 token_transfer 表的接口
 * @author: PandaClark
 * @create: 2019-04-28
 */
public interface TokenMapper {

    String TABLE_NAME = "token_transfer";
    String INSERT_FIELDS = "transfer_id, from_email, to_email, beneficiary_type, transfer_amount, update_time";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{transferId},#{fromEmail},#{toEmail},#{beneficiaryType},#{transferAmount},#{updateTime})"})
    int addTransfer(Token token);
}
