package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import team.educoin.transaction.pojo.Recharge;

import java.util.List;
/**
 * @description: 操作 recharge_apply 表的接口
 * @author: PandaClark
 * @create: 2019-04-28
 */
public interface RechargeMapper {

    String TABLE_NAME = "recharge_apply";
    String INSERT_FIELDS = "email, admin_email, payment_id, payment_method, if_checked, recharge_amount, " +
            "check_time, update_time";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{email},#{adminEmail},#{paymentId},#{paymentMethod},#{ifChecked},#{rechargeAmount},#{checkTime},#{updateTime})"})
    int addRecharge(Recharge recharge);


    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME})
    List<Recharge> getAllRecharges();

}


