package team.educoin.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.TransactionMapper;
import team.educoin.transaction.fabric.FileFabricClient;
import team.educoin.transaction.fabric.TransactionFabricClient;
import team.educoin.transaction.pojo.FileInfo;
import team.educoin.transaction.pojo.fabric.FabricAgencyConsumeInfo;
import team.educoin.transaction.pojo.fabric.FabricUserConsumeInfo;
import team.educoin.transaction.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionFabricClient transactionFabricClient;

    // 普通用户购买资源阅读权
    public String userConsumeService(String email, FileInfo fileInfo){
        FabricUserConsumeInfo fabricUserConsumeInfo = new FabricUserConsumeInfo("org.education.UserConsumeService",
                fileInfo.getId(), email);
        JSONObject jsonObject = null;

        try {
            jsonObject = JSONObject.parseObject((transactionFabricClient.userConsumeService(fabricUserConsumeInfo)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "购买成功";
    }

    // 机构用户购买资源所有权
    public int agencyConsumeService(String email, FileInfo fileInfo){
        FabricAgencyConsumeInfo fabricAgencyConsumeInfo = new FabricAgencyConsumeInfo("org.education.CompanyBuyOnwership",
                fileInfo.getId(), email);
        JSONObject jsonObject = null;
        int res = 0;

        try {
            jsonObject = JSONObject.parseObject((transactionFabricClient.agencyConsumeService(fabricAgencyConsumeInfo)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            res = transactionMapper.updateAgencyConsumer(fileInfo);
        }

        return res;
    }
}
