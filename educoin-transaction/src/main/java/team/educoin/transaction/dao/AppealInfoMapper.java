package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.Appeal;

import java.util.List;

/**
 * @description: appeal_info
 * @author: PandaClark
 * @create: 2019-12-07
 */
@Component
public interface AppealInfoMapper {

    String TABLE_NAME = "appeal_info";

    @Insert("INSERT INTO appeal_info(id,agency_email,file_id,file_name,detail,watermark,if_checked) values(#{id},#{agencyEmail},#{fileId},#{fileName},#{detail},#{watermark},#{ifChecked})")
    int addRecord(Appeal appeal);

    @Select("SELECT * FROM appeal_info WHERE agency_email=#{agency}")
    List<Appeal> getRecordsByAgencyEmail(@Param("agency") String agency);

    @Select("SELECT * FROM appeal_info WHERE if_checked=#{ifChecked}")
    List<Appeal> getRecordsByIfChecked(@Param("ifChecked") int ifChecked);

    @Update({"update ", TABLE_NAME, "set admin_email=#{adminEmail},if_checked=#{checkCode} where id=#{id}"})
    int updateRecordById(@Param("id") String id, @Param("adminEmail") String adminEmail, @Param("checkCode") int checkCode);

}
