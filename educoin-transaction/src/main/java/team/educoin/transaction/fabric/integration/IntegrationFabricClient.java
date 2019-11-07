package team.educoin.transaction.fabric.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @description: 集成 fabric 接口
 * @author: PandaClark
 * @create: 2019-09-20
 */
@Component
@FeignClient(value = "integrationFabricClient",url="${educoin.fabric.url}")
@RequestMapping("/api")
public interface IntegrationFabricClient {


    /**
     * =============================================================
     * @desc 用户使用学豆购买资源，关键信息上链
     * @author PandaClark
     * @date 2019/9/20 11:19 AM
     * @params String 上链信息
     * @return java.util.Map<java.lang.String , java.lang.String>
     * =============================================================
     */
    @RequestMapping( value = "/RegisterUserBuyService", method = RequestMethod.POST )
    Map<String,String> buyServiceFabric(@RequestBody Map<String,String> purchaseInfo);
}
