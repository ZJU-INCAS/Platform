package team.educoin.transaction.service;

import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Token;

/**
 * @description: 管理员Service
 * @author: PandaClark
 * @create: 2019-05-12
 */
public interface AdminService {


    /**
     * =============================================================
     * @desc 查看中央账户
     * @author PandaClark
     * @date 2019/5/12 5:44 PM
     * @return CentralBankDto 中央账户信息的数据传输对象
     * =============================================================
     */
    CentralBankDto getCentralBankInfo();

    /**
     * =============================================================
     * @desc 查看权益分配合约
     * @author PandaClark
     * @date 2019/5/12 5:55 PM
     * @return ContractDto
     * =============================================================
     */
    ContractDto getContractInfo();

    /**
     * =============================================================
     * @desc 根据 id 返回一条充值记录
     * @author PandaClark
     * @date 2019/5/12 8:09 PM
     * @param id 充值记录id
     * @return team.educoin.transaction.pojo.Token
     * =============================================================
     */
    Recharge getRechargeRecordById(String id);

    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/5/12 9:52 PM
     * @return void
     * =============================================================
     */
    void acceptUserRecharge(String paymentId, String adminEmail);

    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/5/12 9:52 PM
     * @return void
     * =============================================================
     */
    void rejectUserRecharge(String paymentId, String adminEmail);
}
