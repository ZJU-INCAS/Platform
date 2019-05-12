package team.educoin.transaction.dao;

import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.UserInfo;

import java.util.List;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@Component
public interface UserMapper {
    List<UserInfo> selectAllUser();
}
