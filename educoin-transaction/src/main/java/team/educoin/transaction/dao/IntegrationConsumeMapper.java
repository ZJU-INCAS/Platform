package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.integration.ConsumeRecord;

import java.util.List;
import java.util.Map;

/**
 * @description: 表 integration_consume
 * @author: PandaClark
 * @create: 2019-09-20
 */
@Component
public interface IntegrationConsumeMapper {


    String TABLE_NAME = "integration_consume";
    String INSERT_FIELDS = "user_id,username,service_id,service_name,course_bean,tx_id,transaction_id,create_time";
    String SELECT_FIELDS = INSERT_FIELDS + ", update_time";

    @Insert({"insert into ", TABLE_NAME, "(",INSERT_FIELDS,") values (#{user_id},#{username},#{service_id},#{service_name},#{course_bean},#{tx_id},#{transaction_id},#{create_time})"})
    int addRecord(Map<String, Object> map);

    @Select("select * from integration_consume")
    List<ConsumeRecord> selectAllRecords();


}
