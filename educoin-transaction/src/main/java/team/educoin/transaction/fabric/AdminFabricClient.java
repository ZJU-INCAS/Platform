package team.educoin.transaction.fabric;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;

import java.util.List;
import java.util.Map;

/**
 * @description: 管理员fabric接口
 * @author: PandaClark
 * @create: 2019-05-12
 */
@Component
@FeignClient(value = "adminFabricClient",url="${educoin.fabric.url}")
@RequestMapping("/api")
public interface AdminFabricClient {

    @RequestMapping( value = "/CentralBank", method = RequestMethod.GET )
    List<CentralBankDto> getCentralBankInfo();

    @RequestMapping( value = "/Contract", method = RequestMethod.GET )
    List<ContractDto> getContractInfo();

    @RequestMapping( value = "/CheckUserRecharge", method = RequestMethod.POST )
    Map<String,String> CheckUserRechargeFabric(@RequestBody Map<String,String> rechargeInfo);
}
