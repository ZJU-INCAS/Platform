package team.educoin.transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.educoin.transaction.dto.integration.TransactionDto;
import team.educoin.transaction.fabric.integration.IntegrationFabricClient;
import team.educoin.transaction.pojo.integration.ConsumeRecord;
import team.educoin.transaction.service.IntegrationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 大课题集成接口
 * @author: PandaClark
 * @create: 2019-09-20
 */

@RestController
@RequestMapping("/integration")
@Api(value = "与大课题集成 API 接口", tags = "integration", description = "与大课题集成 API 接口")
public class IntegrationController {


    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IntegrationFabricClient integrationFabricClient;


    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/9/24 3:40 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "用户通过学豆购买资源")
    @RequestMapping( value = "/resource", method = RequestMethod.POST )
    public CommonResponse getUserInfo(@RequestBody TransactionDto dto){
        CommonResponse res;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("$class", "org.education.RegisterUserBuyService");
            map.put("TxID", dto.getTxId());
            map.put("serviceID", dto.getServiceId());
            map.put("userID", dto.getUserId());
            map.put("readPrice", dto.getCourseBean().toString());

            Map<String, String> results = integrationFabricClient.buyServiceFabric(map);
            integrationService.buyService(dto, results.get("transactionId"));
            res = new CommonResponse(0, "success", results.get("transactionId"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }

    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/10/4 3:40 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "发送区块链交易信息")
    @RequestMapping( value = "/records", method = RequestMethod.GET )
    public CommonResponse getRecordsList(){
        CommonResponse res;
        try {
            List<ConsumeRecord> recordList = integrationService.getRecordInfo();
            res = new CommonResponse(0, "success", recordList);
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }

}
