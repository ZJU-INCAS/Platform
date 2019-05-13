package team.educoin.transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.fabric.AgencyFabricClient;
import team.educoin.transaction.pojo.Withdraw;
import team.educoin.transaction.service.AgencyService;

import java.util.List;


/**
 * @description: 处理机构用户访问
 * @author: PandaClark
 * @create: 2019-05-13
 */
@RestController
@RequestMapping("/agency")
@Api(value = "Agency API 接口", tags = "agency", description = "agency API 接口")
public class AgencyController {

    private String email = "ZjuEducation@email.com";

    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyFabricClient agencyFabricClient;

    @ApiOperation(value = "获取所有审核通过的提现记录")
    @RequestMapping( value = "/withdrawList", method = RequestMethod.GET )
    public CommonResponse checkedWithdrawRecords(){
        List<Withdraw> records = agencyService.getAgencyWithdrawRecords(email, 0);
        CommonResponse res = new CommonResponse(0, "success", records);
        return res;
    }

    @ApiOperation(value = "机构用户提现")
    @RequestMapping( value = "/withdraw", method = RequestMethod.POST )
    public CommonResponse withdraw(@RequestParam("amount") double amount) {
        CommonResponse res = new CommonResponse();

        // email 应当从 session 中拿，此处只是测试
        boolean success = agencyService.companyWithdraw(email, amount);
        if (success){
            res.setStatus(0);
            res.setMessage("success");
            res.setData("提现成功");
        } else {
            res.setStatus(1);
            res.setMessage("failed");
            res.setData("提现失败");
        }
        return res;
    }

}
