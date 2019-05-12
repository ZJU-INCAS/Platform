package team.educoin.transaction.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;
import team.educoin.transaction.fabric.AdminFabricClient;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.service.AdminService;

import java.util.HashMap;
import java.util.Map;


/**
 * @description: 管理员Controller
 * @author: PandaClark
 * @create: 2019-05-12
 */
@RestController
@RequestMapping("/admin")
@Api(value = "Admin API 接口", tags = "admin", description = "admin API 接口")
public class AdminController {


    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminFabricClient adminFabricClient;

    String admin = "clark@zju.incas";


    /**
     * =============================================================
     * @desc 同意用户充值
     * @author PandaClark
     * @date 2019/5/12 5:11 PM
     * @param rechargeId 充值记录ID
     * @return CommonResponse
     * =============================================================
     */
    @RequestMapping( value = "/rechargeY", method = RequestMethod.POST )
    public CommonResponse acceptUserRecharge( @RequestParam("rechargeId") String rechargeId ){

        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = adminService.getRechargeRecordById(rechargeId);
        if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.CheckUserRecharge");
            rechargeInfo.put("rechargeID",record.getPaymentId());
            rechargeInfo.put("paymentid",record.getPaymentMethod());
            // fabric 发 post
            Map<String, String> result = adminFabricClient.CheckUserRechargeFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.acceptUserRecharge(record.getPaymentId(), admin);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核通过");
        }
        return res;
    }

    /**
     * =============================================================
     * @desc 拒绝用户充值
     * @author PandaClark
     * @date 2019/5/12 5:17 PM
     * @param rechargeId 充值记录ID
     * @return CommonResponse
     * =============================================================
     */
    @RequestMapping( value = "/rechargeR", method = RequestMethod.POST )
    public CommonResponse rejectUserRecharge( @RequestParam("rechargeId") String rechargeId ){

        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = adminService.getRechargeRecordById(rechargeId);
        if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.CheckUserRecharge");
            rechargeInfo.put("rechargeID",record.getPaymentId());
            rechargeInfo.put("paymentid",record.getPaymentMethod());
            // fabric 发 post
            Map<String, String> result = adminFabricClient.CheckUserRechargeFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.rejectUserRecharge(record.getPaymentId(), admin);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核拒绝");
        }
        return res;
    }


    /**
     * =============================================================
     * @desc 同意机构用户提现
     * @author PandaClark
     * @date 2019/5/12 5:17 PM
     * @param tokenWithdrawId 提现请求ID
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @RequestMapping( value = "/companyWithdrawY", method = RequestMethod.POST )
    public CommonResponse acceptCompanyWithdraw( @RequestParam("tokenWithdrawId") String tokenWithdrawId){
        CommonResponse res = new CommonResponse();


        return res;
    }


    /**
     * =============================================================
     * @desc 拒绝机构用户提现
     * @author PandaClark
     * @date 2019/5/12 5:18 PM
     * @param tokenWithdrawId 提现请求ID
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @RequestMapping( value = "/companyWithdrawR", method = RequestMethod.POST )
    public CommonResponse rejectCompanyWithdraw( @RequestParam("tokenWithdrawId") String tokenWithdrawId){
        CommonResponse res = new CommonResponse();


        return res;
    }

    /**
     * =============================================================
     * @desc 查看中央账户
     * @author PandaClark
     * @date 2019/5/12 5:26 PM
     * =============================================================
     */
    @RequestMapping( value = "/centralBank", method = RequestMethod.GET )
    public CommonResponse centralBank(){
        CommonResponse res = new CommonResponse();

        CentralBankDto info = adminService.getCentralBankInfo();

        res.setStatus(0);
        res.setMessage("success");
        res.setData(info);
        return res;
    }

    /**
     * =============================================================
     * @desc 查看权益分配合约
     * @author PandaClark
     * @date 2019/5/12 5:27 PM
     * =============================================================
     */
    @RequestMapping( value = "/contract", method = RequestMethod.GET )
    public CommonResponse contract(){
        CommonResponse res = new CommonResponse();

        ContractDto info = adminService.getContractInfo();
        res.setStatus(0);
        res.setMessage("success");
        res.setData(info);
        return res;
    }

}
