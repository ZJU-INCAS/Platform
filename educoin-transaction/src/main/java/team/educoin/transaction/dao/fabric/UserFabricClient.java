package team.educoin.transaction.dao.fabric;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@Component
@FeignClient(value = "fabricUserClient",url="${fabric.url}")
@RequestMapping("/api")
public interface UserFabricClient {
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    String getUser();
}
