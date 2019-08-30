package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.educoin.transaction.dao.AgencyConsumeMapper;
import team.educoin.transaction.dao.AgencyInfoMapper;
import team.educoin.transaction.dao.FileInfoMapper;
import team.educoin.transaction.dao.WithdrawMapper;
import team.educoin.transaction.dto.AgencyWithdrawDto;
import team.educoin.transaction.fabric.AgencyFabricClient;
import team.educoin.transaction.pojo.AgencyInfo;
import team.educoin.transaction.pojo.FileInfo;
import team.educoin.transaction.pojo.UserInfo;
import team.educoin.transaction.pojo.Withdraw;
import team.educoin.transaction.service.AgencyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private AgencyInfoMapper agencyInfoMapper;
    @Autowired
    private AgencyConsumeMapper agencyConsumeMapper;
    @Autowired
    private FileInfoMapper fileInfoMapper;

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
        int i = withdrawMapper.addRecord(withdraw);
        AgencyInfo agencyInfo = agencyInfoMapper.selectRecordById(email);
        int j = agencyInfoMapper.updateAccountBalanceById(email, (agencyInfo.getAccountBalance() + amount));
        return (i + j) > 1;
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

    @Override
    public AgencyInfo getAgencyById(String email) {
        AgencyInfo agencyInfo = agencyInfoMapper.selectRecordById(email);
        return agencyInfo;
    }

    @Override
    @Transactional
    public void agencyBuyOwnership(String email, String serviceID, FileInfo fileInfo, String transactionId) {
        // 原资源拥有者余额增加
        AgencyInfo provider = agencyInfoMapper.selectRecordById(fileInfo.getFileOwner());
        agencyInfoMapper.updateAccountBalanceById(provider.getEmail(), (provider.getAccountBalance() + fileInfo.getFileOwnerShipPrice()));
        // 购买者余额扣除资源价格
        AgencyInfo buyer = agencyInfoMapper.selectRecordById(email);
        agencyInfoMapper.updateAccountBalanceById(email, (buyer.getAccountBalance() - fileInfo.getFileOwnerShipPrice()));
        // 记录机构用户消费记录
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("service_id",fileInfo.getId());
        map.put("file_title",fileInfo.getFileTitle());
        map.put("file_ownerPrice",fileInfo.getFileOwnerShipPrice());
        map.put("file_name",fileInfo.getFileName());
        map.put("transaction_id",fileInfo.getFileName());
        agencyConsumeMapper.addRecord(map);
        // 修改资源所有权
        fileInfoMapper.updateFileOwner(serviceID,email);
    }

    @Override
    public List<AgencyInfo> getAgencyList() {
        return agencyInfoMapper.selectAllRecords();
    }


    @Override
    public boolean registerCompany(AgencyInfo agencyInfo) {
        int i = agencyInfoMapper.addRecord(agencyInfo);
        return i>0;
    }

    @Override
    public boolean deleteAgency(String email) {
        int i = agencyInfoMapper.deleteById(email);
        return i > 0;
    }

    @Override
    public boolean updateAgencyInfo(AgencyInfo agencyInfo) {
        int i = agencyInfoMapper.updateRecord(agencyInfo);
        return i > 0;
    }

    @Override
    public List<String> getAgencyConsumeServiceIds(String email) {
        List<String> ids = agencyConsumeMapper.getServiceIdsByEmail(email);
        return ids;
    }

}
