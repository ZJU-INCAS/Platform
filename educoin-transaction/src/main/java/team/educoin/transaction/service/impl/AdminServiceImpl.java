package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.RechargeMapper;
import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;
import team.educoin.transaction.fabric.AdminFabricClient;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Token;
import team.educoin.transaction.service.AdminService;

import java.util.List;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-05-12
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminFabricClient adminFabricClient;
    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public CentralBankDto getCentralBankInfo() {
        List<CentralBankDto> centralBankDtos = adminFabricClient.getCentralBankInfo();
        return centralBankDtos.get(0);
    }

    @Override
    public ContractDto getContractInfo() {
        List<ContractDto> contractInfos = adminFabricClient.getContractInfo();
        return contractInfos.get(0);
    }

    @Override
    public Recharge getRechargeRecordById(String id) {
        Recharge record = rechargeMapper.getRecordByPaymentId(id);
        return record;
    }

    @Override
    public void acceptUserRecharge(String paymentId, String adminEmail) {
        rechargeMapper.updateRecordByPaymentId(paymentId, adminEmail, 1);
    }

    @Override
    public void rejectUserRecharge(String paymentId, String adminEmail) {
        rechargeMapper.updateRecordByPaymentId(paymentId, adminEmail, 2);
    }
}
