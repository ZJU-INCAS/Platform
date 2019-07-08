package team.educoin.transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;
import team.educoin.transaction.fabric.AdminFabricClient;
import team.educoin.transaction.fabric.AgencyFabricClient;
import team.educoin.transaction.fabric.FileFabricClient;
import team.educoin.transaction.pojo.AdminInfo;
import team.educoin.transaction.pojo.FileInfo;
import team.educoin.transaction.pojo.Recharge;
import team.educoin.transaction.pojo.Withdraw;
import team.educoin.transaction.service.AdminService;
import team.educoin.transaction.service.AgencyService;
import team.educoin.transaction.service.FileService;
import team.educoin.transaction.service.UserService;
import team.educoin.transaction.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 管理员Controller
 * @author: Messi-Q
 * @create: 2019-05-27
 */
@RestController
@RequestMapping("/admin")
@Api(value = "Admin API 接口", tags = "admin", description = "admin API 接口")
public class AdminController {


    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private FileService fileService;
    @Autowired
    private AdminFabricClient adminFabricClient;
    @Autowired
    private AgencyFabricClient agencyFabricClient;
    @Autowired
    private FileFabricClient fileFabricClient;


    /**
     * =============================================================
     * @param
     * @author Messi-q
     * @date 2019/6/4 3:40 PM
     * @return
     * =============================================================
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonResponse getUserInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        AdminInfo adminInfo = adminService.getAdminById(email);
        CommonResponse res = new CommonResponse(0, "success", adminInfo);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员获取所有待审核用户充值列表
     * @author Messi-Q
     * @date 2019/5/13 5:40 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员获取所有未审核用户充值记录")
    @ResponseBody
    @RequestMapping(value = "/rechargeList", method = RequestMethod.GET)
    public CommonResponse unCheckedRechargeList() {
        List<Recharge> rechargeList = adminService.getUnCheckedRechargeList();
        CommonResponse res = new CommonResponse(0, "success", rechargeList);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员获取所有待审核机构提现列表
     * @author Messi-Q
     * @date 2019/5/13 5:53 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员获取所有未审核机构提现记录")
    @ResponseBody
    @RequestMapping(value = "/withdrawList", method = RequestMethod.GET)
    public CommonResponse unCheckedWithdrawList() {
        List<Withdraw> withdrawList = adminService.getUnCheckedWithdrawList();
        CommonResponse res = new CommonResponse(0, "success", withdrawList);
        return res;
    }


    /**
     * =============================================================
     * @param rechargeId 充值记录ID
     * @desc 同意用户充值
     * @author Messi-Q
     * @date 2019/5/12 5:11 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员同意用户充值")
    @ResponseBody
    @RequestMapping(value = "/rechargeY", method = RequestMethod.POST)
    public CommonResponse acceptUserRecharge(HttpServletRequest request, @RequestParam("rechargeId") String rechargeId) {

        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = userService.getRechargeRecordById(rechargeId);
        if (record == null) {
            res.setData("没有改充值记录");
        } else if (record.getIfChecked() != 0) {
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class", "org.education.CheckUserRecharge");
            rechargeInfo.put("rechargeID", record.getPaymentId());
            rechargeInfo.put("paymentid", record.getPaymentMethod());
            // fabric 发 post
            Map<String, String> result = adminFabricClient.CheckUserRechargeFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.acceptUserRecharge(record.getPaymentId(), email);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核通过");
        }
        return res;
    }

    /**
     * =============================================================
     * @param rechargeId 充值记录ID
     * @desc 拒绝用户充值
     * @author Messi-Q
     * @date 2019/5/12 5:17 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员拒绝用户充值")
    @ResponseBody
    @RequestMapping(value = "/rechargeR", method = RequestMethod.POST)
    public CommonResponse rejectUserRecharge(HttpServletRequest request, @RequestParam("rechargeId") String rechargeId) {

        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = userService.getRechargeRecordById(rechargeId);
        if (record == null) {
            res.setData("没有该充值记录");
        } else if (record.getIfChecked() != 0) {
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class", "org.education.RejectUserRecharge");
            rechargeInfo.put("rechargeID", record.getPaymentId());
            // fabric 发 post
            Map<String, String> result = adminFabricClient.RejectUserRechargeFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.rejectUserRecharge(record.getPaymentId(), email);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核拒绝");
        }
        return res;
    }


    /**
     * =============================================================
     * @param withdrawId 提现记录的 payment_id
     * @desc 同意机构用户提现
     * @author Messi-Q
     * @date 2019/5/12 5:17 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员同意机构用户提现")
    @ResponseBody
    @RequestMapping(value = "/withdrawY", method = RequestMethod.POST)
    public CommonResponse acceptCompanyWithdraw(HttpServletRequest request, @RequestParam("withdrawId") String withdrawId) {

        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Withdraw record = agencyService.getWithdrawRecordById(withdrawId);
        if (record == null) {
            res.setData("没有该提现记录");
        } else if (record.getIfChecked() != 0) {
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class", "org.education.CheckCompanyWithdraw");
            rechargeInfo.put("tokenWithdrawID", record.getPaymentId());
            rechargeInfo.put("paymentid", record.getPaymentMethod());
            // fabric 发 post
            Map<String, String> result = agencyFabricClient.CheckAgencyWithdrawFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.acceptCompanyWithdraw(record.getPaymentId(), email);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核通过");
        }
        return res;
    }


    /**
     * =============================================================
     * @param withdrawId 提现记录的 payment_id
       * @desc 拒绝机构用户提现
     * @author Messi-Q
     * @date 2019/5/12 5:18 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员拒绝机构用户提现")
    @ResponseBody
    @RequestMapping(value = "/withdrawR", method = RequestMethod.POST)
    public CommonResponse rejectCompanyWithdraw(HttpServletRequest request, @RequestParam("withdrawId") String withdrawId) {

        CommonResponse res = new CommonResponse();
        String email = (String) request.getAttribute("email");
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Withdraw record = agencyService.getWithdrawRecordById(withdrawId);
        if (record == null) {
            res.setData("没有改提现记录");
        } else if (record.getIfChecked() != 0) {
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class", "org.education.RejectCompanyWithdraw");
            rechargeInfo.put("tokenWithdrawID", record.getPaymentId());
            // fabric 发 post
            Map<String, Object> result = agencyFabricClient.RejectAgencyWithdrawFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            adminService.rejectCompanyWithdraw(record.getPaymentId(), email);
            res.setStatus(0);
            res.setMessage("success");
            res.setData("已审核拒绝");
        }
        return res;
    }


    /**
     * =============================================================
     * @desc 查看中央账户
     * @author Messi-Q
     * @date 2019/5/12 5:26 PM
     * =============================================================
     */
    @ApiOperation(value = "获取中央账户信息")
    @ResponseBody
    @RequestMapping(value = "/centralBank", method = RequestMethod.GET)
    public CommonResponse centralBank() {
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
     * @author Messi-Q
     * @date 2019/5/12 5:27 PM
     * =============================================================
     */
    @ApiOperation(value = "获取权益分配合约")
    @ResponseBody
    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public CommonResponse contract() {
        CommonResponse res = new CommonResponse();

        ContractDto info = adminService.getContractInfo();
        res.setStatus(0);
        res.setMessage("success");
        res.setData(info);
        return res;
    }


    /**
     * =============================================================
     * @desc 查看所有资源列表
     * @author Messi-Q
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "查看所有资源列表", notes = "查看所有资源列表")
    @ResponseBody
    @RequestMapping(value = "/resourcelist", method = RequestMethod.GET)
    public CommonResponse resourceList() {
        List<FileInfo> files = fileService.getServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看待审核列表
     * @author Messi-Q
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看待审核列表", notes = "管理员查看待审核列表")
    @ResponseBody
    @RequestMapping(value = "/resourcelistW", method = RequestMethod.GET)
    public CommonResponse resourceListW() {
        List<FileInfo> files = fileService.getUnCheckedServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看审核通过记录
     * @author Messi-Q
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看审核通过记录", notes = "管理员查看审核通过记录")
    @ResponseBody
    @RequestMapping(value = "/resourcelistY", method = RequestMethod.GET)
    public CommonResponse resourceListY() {
        List<FileInfo> files = fileService.getCheckedServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看审核拒绝记录
     * @author Messi-Q
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看审核拒绝记录", notes = "管理员查看审核拒绝记录")
    @ResponseBody
    @RequestMapping(value = "/resourcelistR", method = RequestMethod.GET)
    public CommonResponse resourceListR() {
        List<FileInfo> files = fileService.getRejectServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }


    /**
     * =============================================================
     * @desc 管理员审核通过资源
     * @author Messi-Q
     * @date 2019/5/16 10:16 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员审核通过资源", notes = "管理员查看审核拒绝记录")
    @ResponseBody
    @RequestMapping(value = "/serviceY", method = RequestMethod.GET)
    public CommonResponse checkService(HttpServletRequest request, @RequestParam("id") String id) {

        CommonResponse res = null;
        String email = (String) request.getAttribute("email");

        try {
            FileInfo fileInfo = fileService.getFileInfoById(id);
            // 机构用户上传资源时，信息不上链，基本信息只存在数据库里，只有审核通过的资源才上链
            Map<String, Object> map = new HashMap<>();
            map.put("$class", "org.education.RegisterService");
            map.put("serviceID", fileInfo.getId());
            map.put("serviceName", fileInfo.getFileTitle());
            map.put("readPrice", fileInfo.getFileReadPrice());
            map.put("ownershipPrice", fileInfo.getFileOwnerShipPrice());
            map.put("company", fileInfo.getFileInitialProvider());

            fileFabricClient.registerService(map);
            adminService.acceptService(email, id);
            res = new CommonResponse(0, "success", "注册新资源");
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }

        return res;
    }

    /**
     * =============================================================
     * @desc 管理员审核拒绝资源
     * @author Messi-Q
     * @date 2019/5/16 10:16 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员审核拒绝资源", notes = "管理员查看审核拒绝记录")
    @ResponseBody
    @RequestMapping(value = "/serviceR", method = RequestMethod.GET)
    public CommonResponse rejectService(HttpServletRequest request, @RequestParam("id") String id) {
        String email = (String) request.getAttribute("email");
        adminService.rejectService(email, id);
        CommonResponse res = new CommonResponse(0, "success", "已审核拒绝");
        return res;
    }

    /*
     * =============================================================
     * @Description 管理员验证被侵权图片资源的水印信息
     * @Author Messi-Q
     * @Date 19:32 2019-05-27
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     **/
    @ApiOperation(value = "管理员处理资源侵权", notes = "管理员验证被侵权图片资源的水印信息")
    @ResponseBody
    @RequestMapping(value = "/validateWatermarkInfo/{id}", method = RequestMethod.GET)
    public CommonResponse extractServiceInfo(@PathVariable("id") String id) throws IOException, InterruptedException {
        CommonResponse res = new CommonResponse(0, "success", "提取成功");

        // 逻辑：验证的时候：当前资源所有者email+当前资源使用者email+资源id
        // 将当前所有者和使用者的email统一置为当前使用者email，若验证成功，则未侵权；若验证不成功，则侵权
        // 如果已知当前侵权嫌疑人是谁，则直接验证

        String email = "qian";  // 当前侵权嫌疑人email, 将该email和所有者email设为一致进行校验

        // 根据文件id获取文件名
        FileInfo fileInfo = fileService.getFileInfoById(id);
        String filename = fileInfo.getFileName();

        //validate watermark
        String waterMarkEmbedTool = ResourceUtils.getURL("classpath:static/watermark_image/image_watermark_extract.py").getPath();
        String waterMarkValidateInfo = email + "-" + email + "-" + id;
        String fileInfirnged = FileUtil.DOWNLOAD_DIR + "/" + filename;

        // 调用python脚本
        String commond = String.format("python %s %s %s", waterMarkEmbedTool, waterMarkValidateInfo, fileInfirnged);
        Process process = Runtime.getRuntime().exec(commond);
        process.waitFor();
        BufferedInputStream in = new BufferedInputStream(process.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        String result = null;
        while ((line = br.readLine()) != null) {
            result = line;
        }
        br.close();
        in.close();
        System.out.println(result);  // 打印验证结果

        return res;
    }

    /*
     * =============================================================
     * @Description 管理员提取被侵权资源的水印信息(图片和pdf)
     * @Messi-q
     * @Date 00:35 2019-06-14
     * @Param []
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     **/
    @ApiOperation(value = "管理员处理资源侵权", notes = "管理员提取资源中的水印信息")
    @ResponseBody
    @RequestMapping(value = "/extractWatermarkInfo/{id}", method = RequestMethod.GET)
    public CommonResponse extractWatermarkInfo(@PathVariable("id") String id) throws IOException, InterruptedException {
        CommonResponse res = new CommonResponse(0, "success", "提取成功");

        // 根据文件id获取文件名
        FileInfo fileInfo = fileService.getFileInfoById(id);
        String filename = fileInfo.getFileName();

        // 获取文件类型(后缀名)
        String[] allowImageTypes = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        boolean imageContain = Arrays.asList(allowImageTypes).contains(type);

        // 提取image水印
        if (imageContain) {
            //extrac imgae watermarkInfo
            String waterMarkEmbedTool = ResourceUtils.getURL("classpath:static/watermark/image_watermark_extract.py").getPath();
            String outFile = filename.substring(0, filename.lastIndexOf("."));  // 获取不带后缀名的文件名
            String fileInfirnged = FileUtil.DOWNLOAD_DIR + "/" + outFile + ".png";  // 获取需要提取水印的文件路径

            // 调用python脚本
            String commond = String.format("python %s %s", waterMarkEmbedTool, fileInfirnged);
            Process process = Runtime.getRuntime().exec(commond);
            process.waitFor();
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            String result = null;
            while ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
            in.close();
            System.out.println(result);  // 打印水印信息
        } else if (type.equals("pdf")) {
            //extract image watermarkInfo
            String waterMarkEmbedTool = ResourceUtils.getURL("classpath:static/watermark/pdf_watermark_extract.py").getPath();
            String fileInfirnged = FileUtil.DOWNLOAD_DIR + "/" + filename;

            // 调用python脚本
            String commond = String.format("python %s %s", waterMarkEmbedTool, fileInfirnged);
            Process process = Runtime.getRuntime().exec(commond);
            process.waitFor();
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            String result = null;
            while ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
            in.close();
            System.out.println(result);  // 打印水印信息
        } else {
            System.out.println("水印暂时只支持图片和PDF文档！");
        }

        return res;
    }
}



















