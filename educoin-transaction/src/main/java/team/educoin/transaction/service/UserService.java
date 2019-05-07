package team.educoin.transaction.service;

import team.educoin.transaction.dto.TokenTransferDto;

import java.util.Map;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
public interface UserService {
    /**
     * 测试服务器是否能通：从 fabric 和 mysql 中读出数据
     * @return
     */
    Map<String, Object> getUserInfo();

    /**
     * ===================================================================
     * @desc 普通用户充值
     * @author PandaClark
     * @param email 充值账户ID
     * @param balance 充值金额
     * ===================================================================
     */
    boolean userRecharge(String email, double balance);

    /**
     * ===================================================================
     * @desc 普通用户向普通用户转账
     * @author PandaClark
     * @param dto 保存转账操作相关数据的对象
     * ===================================================================
     */
    boolean tokenTransferU2U(TokenTransferDto dto);


    /**
     * ===================================================================
     * @desc 普通用户向机构用户转账
     * @author PandaClark
     * @param dto 保存转账操作相关数据的对象
     * ===================================================================
     */
    boolean tokenTransferU2C(TokenTransferDto dto);
}
