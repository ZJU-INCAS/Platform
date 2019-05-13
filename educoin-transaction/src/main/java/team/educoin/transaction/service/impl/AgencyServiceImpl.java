package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.WithdrawMapper;
import team.educoin.transaction.dto.AgencyWithdrawDto;
import team.educoin.transaction.fabric.AgencyFabricClient;
import team.educoin.transaction.pojo.Withdraw;
import team.educoin.transaction.service.AgencyService;

import java.util.List;

import static team.educoin.transaction.util.UUIDutil.getUUID;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-05-13
 */
@Service
public class AgencyServiceImpl implements AgencyService {

    @Autowired
    private AgencyFabricClient agencyFabricClient;
    @Autowired
    private WithdrawMapper withdrawMapper;

    /**
     * =============================================================
     * @desc 机构用户提现
     * @author PandaClark
     * @date 2019/5/13 4:36 PM
     * @param email 提现账户
     * @param amount 提现金额
     * @return boolean
     * =============================================================
     */
    @Override
    public boolean companyWithdraw(String email, double amount) {
        String paymentID = getUUID();
        // 1. 向 fabric 发 post 请求
        AgencyWithdrawDto dto = new AgencyWithdrawDto("org.education.CompanyWithdraw", amount, paymentID, email);

        try {
            AgencyWithdrawDto response = agencyFabricClient.companyWithdrawFabric(dto);
        } catch ( Exception e ){
            e.printStackTrace();
            return false;
        }
        // 2. 将记录写入 mysql
        Withdraw withdraw = new Withdraw(email, paymentID, "alipay",amount);
        int insert = withdrawMapper.addRecord(withdraw);
        return insert > 0;
    }

    /**
     * =============================================================
     * @desc 根据 payment_id 返回一条记录
     * @author PandaClark
     * @date 2019/5/13 4:36 PM
     * @param id payment_id
     * @return team.educoin.transaction.pojo.Withdraw
     * =============================================================
     */
    @Override
    public Withdraw getWithdrawRecordById(String id) {
        Withdraw record = withdrawMapper.getRecordByPaymentId(id);
        return record;
    }

    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/5/13 6:02 PM
     * @param email 用户ID
     * @param flag 提现记录审核状态： 0 待审核/1 通过/2 拒绝
     * @return java.util.List<Withdraw>
     * =============================================================
     */
    @Override
    public List<Withdraw> getAgencyWithdrawRecords(String email, int flag) {
        // 从 mysql 中查结果
        List<Withdraw> records = withdrawMapper.getRecordsByIdAndFlag(email, flag);
        return records;
    }
}
