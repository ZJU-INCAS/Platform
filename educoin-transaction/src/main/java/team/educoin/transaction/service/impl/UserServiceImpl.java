package team.educoin.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.educoin.transaction.dao.UserMapper;
import team.educoin.transaction.fabric.UserFabricClient;
import team.educoin.transaction.service.UserService;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, Object> getUserInfo() {
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("fabricUserInfo", userFabricClient.getUser());
        userInfoMap.put("mysqlUserInfo",userMapper.selectAllUser() );
        return userInfoMap;
    }
}
