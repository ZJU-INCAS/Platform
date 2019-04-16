package team.educoin.transaction.fabric;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.educoin.transaction.pojo.FabricUserInfo;

import java.util.List;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@Component
@FeignClient(value = "fabricUserClient",url="${educoin.fabric.url}")
@RequestMapping("/api")
public interface UserFabricClient {
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    List<FabricUserInfo> getUser();
}
