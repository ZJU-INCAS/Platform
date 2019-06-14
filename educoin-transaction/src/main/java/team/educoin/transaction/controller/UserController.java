package team.educoin.transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.dto.TokenTransferDto;
import team.educoin.transaction.fabric.UserFabricClient;
import team.educoin.transaction.pojo.FileInfo;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Token;
import team.educoin.transaction.pojo.UserInfo;
import team.educoin.transaction.service.FileService;
import team.educoin.transaction.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Api(value = "User API 接口", tags = "user", description = "user API 接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserFabricClient userFabricClient;


    /**
     * 测试服务器IP 和 数据库 是否能通
     */
    @RequestMapping( value = "/alluser", method = RequestMethod.GET)
    public CommonResponse testFabricRequest() {
        CommonResponse res = new CommonResponse();
        res.setStatus(0);
        res.setMessage("success");
        res.setData(userService.getUserInfo());
        return res;
    }

    /**
     * =============================================================
     * @author Messi-q
     * @date 2019/6/4 3:40 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping( value = "/detail", method = RequestMethod.GET )
    public CommonResponse getUserInfo(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        UserInfo userInfo = userService.getUserById(email);
        CommonResponse res = new CommonResponse(0, "success", userInfo);
        return res;
    }

    /**
     * =============================================================
     * @author Messi-q
     * @date 2019/5/12 12:20 PM
     * @param []
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取所有审核通过的充值记录")
    @RequestMapping( value = "/getRechargesY", method = RequestMethod.GET )
    public CommonResponse getRechargesY(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        List<Recharge> recharges = userService.getUserRechargeRecords(email, 0);
        CommonResponse res = new CommonResponse(0, "success", recharges);
        return res;
    }

    /**
     * @author Messi-q
     * @desc 普通用户充值
     * @param balance 充值金额
     */
    @ApiOperation(value = "用户充值", notes = "用户充值接口")
    @RequestMapping( value = "/recharge", method = RequestMethod.POST )
    public CommonResponse recharge(HttpServletRequest request, @RequestParam("balance") double balance) {
        CommonResponse res = new CommonResponse();

        String email = (String) request.getAttribute("email");
        boolean success = userService.userRecharge(email, balance);
        if (success){
            res.setStatus(0);
            res.setMessage("success");
            res.setData("充值成功");
        } else {
            res.setStatus(1);
            res.setMessage("failed");
            res.setData("充值失败");
        }
        return res;
    }


    /**
     * =============================================================
     * @author Messi-q
     * @date 2019/5/12 1:20 PM
     * @param []
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取所有转账记录")
    @RequestMapping( value = "/transferList", method = RequestMethod.GET )
    public CommonResponse getTransferRecords(HttpServletRequest request){
        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        List<Token> transfers = userService.getUserTransferRecords(email);
        res.setStatus(0);
        res.setMessage("success");
        res.setData(transfers);
        return res;
    }

    /**
     * @author Messi-q
     * @desc 普通用户向普通用户转账
     * @param transferDto 将转账信息存储到 transferDto 对象中
     * @return
     */
    @ApiOperation(value = "普通用户向普通用户转账", notes = "普通用户向普通用户转账接口")
    @RequestMapping( value = "/transferu2u", method = RequestMethod.POST )
    public CommonResponse transferU2U(HttpServletRequest request, @RequestBody TokenTransferDto transferDto){
        CommonResponse res = new CommonResponse();

        String email = (String) request.getAttribute("email");

        transferDto.setFromuser(email);

        boolean success = userService.tokenTransferU2U(transferDto);
        if (success){
            res.setStatus(0);
            res.setMessage("success");
            res.setData("充值成功");
        } else {
            res.setStatus(1);
            res.setMessage("failed");
            res.setData("充值失败");
        }
        return res;
    }


    /**
     * @author Messi-q
     * @desc 普通用户向机构用户转账
     * @param transferDto 将转账信息存储到 transferDto 对象中
     * @return
     */
    @ApiOperation(value = "普通用户向机构用户转账", notes = "普通用户向机构用户转账接口")
    @RequestMapping( value = "/transferu2c", method = RequestMethod.POST )
    public CommonResponse transferU2C(HttpServletRequest request, @RequestBody TokenTransferDto transferDto){
        CommonResponse res = new CommonResponse();

        String email = (String) request.getAttribute("email");

        transferDto.setFromuser(email);

        boolean success = userService.tokenTransferU2C(transferDto);
        if (success){
            res.setStatus(0);
            res.setMessage("success");
            res.setData("充值成功");
        } else {
            res.setStatus(1);
            res.setMessage("failed");
            res.setData("充值失败");
        }
        return res;
    }

    /**
     * =============================================================
     * @desc 查询可购买资源列表
     * @author Messi-q
     * @date 2019/5/16 12:39 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "查询可购买资源列表", notes = "查询可购买资源列表")
    @RequestMapping( value = "/myresourcelist", method = RequestMethod.GET )
    public CommonResponse resourceList(){
        List<FileInfo> files = fileService.getCheckedServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 查询已购买资源列表
     * @author Messi-q
     * @date 2019/6/4 11:24 AM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "查询已购买资源列表", notes = "查询已购买资源列表")
    @RequestMapping( value = "/resourcelist", method = RequestMethod.GET )
    public CommonResponse myResourceList(HttpServletRequest request){

        String email = (String) request.getAttribute("email");
        List<String> resourcesIds = userService.getUserConsumeServiceIds(email);
        List<FileInfo> files = new ArrayList<>();

        for (String id : resourcesIds) {
            FileInfo fileInfo = fileService.getFileInfoById(id);
            files.add(fileInfo);
        }
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }



    /**
     * =============================================================
     * @desc 普通用户购买阅读权
     * @author Messi-q
     * @date 2019/5/15 7:11 PM
     * @param
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "普通用户购买阅读权")
    @RequestMapping( value = "/consume", method = RequestMethod.POST )
    public CommonResponse consume(HttpServletRequest request, @RequestParam("serviceID") String serviceID ){
        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        UserInfo user = userService.getUserById(email);
        FileInfo fileInfo = fileService.getFileInfoById(serviceID);
        if (fileInfo.getFileReadPrice() > user.getAccountBalance() ){
            res.setStatus(1);
            res.setMessage("failed");
            res.setData("余额不足");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("$class","org.education.UserConsumeService");
            map.put("serviceID",serviceID);
            map.put("user",email);
            try {
                Map<String, Object> map1 = userFabricClient.userConsumeService(map);
                userService.userConsumeService(email,serviceID,fileInfo);
                res.setStatus(0);
                res.setMessage("success");
                res.setData(map1);
            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus(1);
                res.setMessage("failed");
                res.setData(e.getMessage());
            }
        }
        return res;
    }


    @RequestMapping( value = "/test", method = RequestMethod.GET)
    public CommonResponse test(){
        CommonResponse response = new CommonResponse();
        Map<String, String> map = new HashMap<>();
        map.put("$class","org.education.UserConsumeService");
        map.put("serviceID","ceshi12345678");
        map.put("user","test2@qq.com");

        try {
            Map<String, Object> res = userFabricClient.userConsumeService(map);
            System.out.println("response map: "+ res);
        } catch (Exception e) {
            System.out.println("exception occurs");
            e.printStackTrace();
        }

        return response;
    }

    // 测试加入 jwt 验证后，通过拦截器是否能拿到用户ID
    @RequestMapping(value = "/jwt", method = RequestMethod.GET)
    public String jwtTest(HttpServletRequest request){

        String email = (String) request.getAttribute("email");

        System.out.println("email:" + email);

        return email;
    }
}
