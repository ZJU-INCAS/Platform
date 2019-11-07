package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.IntegrationConsumeMapper;
import team.educoin.transaction.dto.integration.TransactionDto;
import team.educoin.transaction.pojo.integration.ConsumeRecord;
import team.educoin.transaction.service.IntegrationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 集成接口实现类
 * @author: PandaClark
 * @create: 2019-09-20
 */
@Service
public class IntegrationServiceImpl implements IntegrationService {


    @Autowired
    private IntegrationConsumeMapper integrationConsumeMapper;

    @Override
    public Boolean buyService(TransactionDto dto, String transactionId) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", dto.getUserId());
        map.put("username", dto.getUsername());
        map.put("service_id", dto.getServiceId());
        map.put("service_name", dto.getServiceName());
        map.put("course_bean", dto.getCourseBean());
        map.put("tx_id", dto.getTxId());
        map.put("transaction_id", transactionId);
        map.put("create_time", dto.getTimestamp());

        int i = integrationConsumeMapper.addRecord(map);
        return i > 0;
    }

    @Override
    public List<ConsumeRecord> getRecordInfo() {
        List<ConsumeRecord> records = integrationConsumeMapper.selectAllRecords();
        return records;
    }
}
