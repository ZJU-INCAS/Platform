package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.RechargeMapper;
import team.educoin.transaction.dao.TokenMapper;
import team.educoin.transaction.dao.UserMapper;
import team.educoin.transaction.dto.TokenTransferDto;
import team.educoin.transaction.dto.UserRechargeDto;
import team.educoin.transaction.fabric.UserFabricClient;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Token;
import team.educoin.transaction.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static team.educoin.transaction.util.UUIDutil.getUUID;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserFabricClient userFabricClient;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public Map<String, Object> getUserInfo() {
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("fabricUserInfo", userFabricClient.getUser());
        userInfoMap.put("mysqlUserInfo",userMapper.selectAllUser());
        return userInfoMap;
    }


    /**
     * ===================================================================
     * @desc 普通用户充值
     * @author PandaClark
     * @param email 充值账户ID
     * @param balance 充值金额
     * ===================================================================
     */
    @Override
    public boolean userRecharge(String email, double balance) {
        // 0. 请求第三方支付，返回第三方支付id，暂时用 UUID 代替
        String paymentID = getUUID();

        // 1. 向 fabric 发 post 请求
        UserRechargeDto dto = new UserRechargeDto("org.education.UserRecharge", balance, paymentID, "resource:org.education.User#" + email);
        // 测试抛异常的测试用例
        // UserRechargeDto dto = new UserRechargeDto("org.education.UserRecharge", balance, paymentID, "resource:org.education.User#" + email);

        try {
            UserRechargeDto response = userFabricClient.userRechargeFabric(dto);
        } catch ( Exception e ){
            e.printStackTrace();
            return false;
        }

        // 2. 将记录写入 mysql
        Recharge recharge = new Recharge(email, paymentID, "alipay",balance);
        int insert = rechargeMapper.addRecharge(recharge);
        return insert > 0;
    }


    /**
     * ===================================================================
     * @desc 普通用户向普通用户转账
     * @author PandaClark
     * @param dto 保存转账操作相关数据的对象
     * ===================================================================
     */
    @Override
    public boolean tokenTransferU2U(TokenTransferDto dto) {

        dto.setClassName("org.education.TokenTransferU_U");
        dto.setTransferID(getUUID());

        // 1. 向 fabric 发 post 请求
        try {
            // 能接收到返回值说明 fabric 200 OK
            TokenTransferDto responseDto = userFabricClient.tokenTransferU_UFabric(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 2. 将记录写入 mysql
        Token token = new Token(dto.getTransferID(), dto.getFromuser(), dto.getTo(), 0, dto.getTransferNum());
        int insert = tokenMapper.addTransfer(token);
        return insert > 0;
    }



    /**
     * ===================================================================
     * @desc 普通用户向机构用户转账
     * @author PandaClark
     * @param dto 保存转账操作相关数据的对象
     * ===================================================================
     */
    @Override
    public boolean tokenTransferU2C(TokenTransferDto dto) {
        dto.setClassName("org.education.TokenTransferU_C");
        dto.setTransferID(getUUID());

        // 1. 向 fabric 发 post 请求
        try {
            // 能接收到返回值说明 fabric 200 OK
            TokenTransferDto responseDto = userFabricClient.tokenTransferU_UFabric(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 2. 将记录写入 mysql
        Token token = new Token(dto.getTransferID(), dto.getFromuser(), dto.getTo(), 1, dto.getTransferNum());
        int insert = tokenMapper.addTransfer(token);
        return insert > 0;
    }
}
