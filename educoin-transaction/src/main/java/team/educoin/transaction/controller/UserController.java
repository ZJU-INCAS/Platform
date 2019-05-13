package team.educoin.transaction.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.dto.TokenTransferDto;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Token;
import team.educoin.transaction.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/user")
@Api(value = "User API 接口", tags = "user", description = "user API 接口")
public class UserController {

    @Autowired
    private UserService userService;


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
     * @author PandaClark
     * @date 2019/5/12 12:20 PM
     * @param []
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取所有审核通过的充值记录")
    @RequestMapping( value = "/getRechargesY", method = RequestMethod.GET )
    public CommonResponse getRechargesY(){
        // email 应当从 session 中拿，此处只是测试
        String email = "test1@qq.com";
        List<Recharge> recharges = userService.getUserRechargeRecords(email, 0);
        CommonResponse res = new CommonResponse(0, "success", recharges);
        return res;
    }

    /**
     * @author PandaClark
     * @desc 普通用户充值
     * @param balance 充值金额
     */
    @ApiOperation(value = "用户充值", notes = "用户充值接口")
    @ApiResponses({
            @ApiResponse(code = 0, message = "success"),
            @ApiResponse(code = 1, message = "failed")
    })
    // @ApiImplicitParam(name = "username", value = "用户名", required = true, dataTypeClass = String.class )
    @RequestMapping( value = "/recharge", method = RequestMethod.POST )
    public CommonResponse recharge(@RequestParam("balance") double balance) {
        CommonResponse res = new CommonResponse();

        // email 应当从 session 中拿，此处只是测试
        String email = "test1@qq.com";
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
     * @author PandaClark
     * @date 2019/5/12 1:20 PM
     * @param []
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取所有转账记录")
    @RequestMapping( value = "/transferList", method = RequestMethod.GET )
    public CommonResponse getTransferRecords(){
        CommonResponse res = new CommonResponse();
        // email 应当从 session 中拿，此处只是测试
        String email = "test1@qq.com";
        List<Token> transfers = userService.getUserTransferRecords(email);
        res.setStatus(0);
        res.setMessage("success");
        res.setData(transfers);
        return res;
    }

    /**
     * @author PandaClark
     * @desc 普通用户向普通用户转账
     * @param transferDto 将转账信息存储到 transferDto 对象中
     * @return
     */
    @ApiOperation(value = "普通用户向普通用户转账", notes = "普通用户向普通用户转账接口")
    @RequestMapping( value = "/transferu2u", method = RequestMethod.POST )
    public CommonResponse transferU2U(@RequestBody TokenTransferDto transferDto){
        CommonResponse res = new CommonResponse();

        // email 应当从 session 中拿，此处只是测试
        String email = "test1@qq.com";

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
     * @author PandaClark
     * @desc 普通用户向机构用户转账
     * @param transferDto 将转账信息存储到 transferDto 对象中
     * @return
     */
    @ApiOperation(value = "普通用户向机构用户转账", notes = "普通用户向机构用户转账接口")
    // @ApiResponses({
    //         @ApiResponse(code = 0, message = "success"),
    //         @ApiResponse(code = 1, message = "failed")
    // })
    @RequestMapping( value = "/transferu2c", method = RequestMethod.POST )
    public CommonResponse transferU2C(@RequestBody TokenTransferDto transferDto){
        CommonResponse res = new CommonResponse();

        // email 应当从 session 中拿，此处只是测试
        String email = "test1@qq.com";

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
}
