package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.AppealInfoMapper;
import team.educoin.transaction.pojo.Appeal;
import team.educoin.transaction.service.AppealService;

import java.util.List;

/**
 * @description: 侵权上诉接口的实现类
 * @author: PandaClark
 * @create: 2019-12-07
 */
@Service
public class AppealServiceImpl implements AppealService {


    @Autowired
    private AppealInfoMapper appealInfoMapper;


    @Override
    public boolean registerAppeal(Appeal appeal) {
        int i = appealInfoMapper.addRecord(appeal);
        return i > 0;
    }

    @Override
    public List<Appeal> getAgencyAppealList(String agency) {
        List<Appeal> records = appealInfoMapper.getRecordsByAgencyEmail(agency);
        return records;
    }

    @Override
    public List<Appeal> getUncheckedAppealList() {
        List<Appeal> records = appealInfoMapper.getRecordsByIfChecked(0);
        return records;
    }

    @Override
    public List<Appeal> getApprovedAppealList() {
        List<Appeal> records = appealInfoMapper.getRecordsByIfChecked(1);
        return records;
    }

    @Override
    public List<Appeal> getRejectAppealList() {
        List<Appeal> records = appealInfoMapper.getRecordsByIfChecked(2);
        return records;
    }

    @Override
    public boolean approveAppeal(String id, String admin) {
        int i = appealInfoMapper.updateRecordById(id, admin, 1);
        return i > 0;
    }

    @Override
    public boolean rejectAppeal(String id, String admin) {
        int i = appealInfoMapper.updateRecordById(id, admin, 2);
        return i > 0;
    }
}
