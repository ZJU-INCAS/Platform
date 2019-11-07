package team.educoin.transaction.service;

import team.educoin.transaction.dto.integration.TransactionDto;
import team.educoin.transaction.pojo.integration.ConsumeRecord;

import java.util.List;

/**
 * @description: 和大课题集成的service
 * @author: PandaClark
 * @create: 2019-09-20
 */
public interface IntegrationService {


    /**
     * =============================================================
     * @desc 用户使用学豆购买资源
     * @author PandaClark
     * @date 2019/9/20 11:04 AM
     * @param
     * @return
     * =============================================================
     */
    Boolean buyService(TransactionDto dto, String transactionId) throws Exception;

    /**
     * =============================================================
     * @desc 获取上链交易记录列表
     * @author PandaClark
     * @date 2019/10/8 2:47 PM
     * @param
     * @return java.util.List<team.educoin.transaction.pojo.integration.ConsumeRecord>
     * =============================================================
     */
    List<ConsumeRecord> getRecordInfo();
}
