package team.educoin.transaction.dao;

import team.educoin.transaction.pojo.UserInfo;

import java.util.List;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
public interface UserMapper {
    List<UserInfo> selectAllUser();
}
