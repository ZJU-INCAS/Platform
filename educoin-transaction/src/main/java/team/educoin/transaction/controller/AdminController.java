package team.educoin.transaction.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.ResourceUtils;
import springfox.documentation.spring.web.json.Json;
import team.educoin.transaction.dto.CentralBankDto;
import team.educoin.transaction.dto.ContractDto;
import team.educoin.transaction.fabric.AdminFabricClient;
import team.educoin.transaction.fabric.AgencyFabricClient;
import team.educoin.transaction.fabric.FileFabricClient;
import team.educoin.transaction.pojo.*;
import team.educoin.transaction.service.*;
import team.educoin.transaction.util.FileUtil;
import team.educoin.transaction.util.MyBeanMapUtil;
import team.educoin.transaction.util.WatermarkUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private AppealService appealService;


    /**
     * =============================================================
     * @author PandaClark
     * @date 2019/6/4 3:40 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping( value = "/detail", method = RequestMethod.GET )
    public CommonResponse getUserInfo(HttpServletRequest request){
        CommonResponse res;
        String email = (String) request.getAttribute("email");
        AdminInfo adminInfo = adminService.getAdminById(email);
        Map<String, Object> map = null;
        try {
            map = MyBeanMapUtil.BeanToMap(adminInfo);
            map.remove("fingerprint");
            map.remove("iris");
            res = new CommonResponse(0, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员获取所有待审核用户充值列表
     * @author PandaClark
     * @date 2019/5/13 5:40 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员获取所有未审核用户充值记录")
    @ResponseBody
    @RequestMapping( value = "/rechargeList", method = RequestMethod.GET )
    public CommonResponse unCheckedRechargeList(){
        List<Recharge> rechargeList = adminService.getUnCheckedRechargeList();
        CommonResponse res = new CommonResponse(0, "success", rechargeList);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员获取所有待审核机构提现列表
     * @author PandaClark
     * @date 2019/5/13 5:53 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员获取所有未审核机构提现记录")
    @ResponseBody
    @RequestMapping( value = "/withdrawList", method = RequestMethod.GET )
    public CommonResponse unCheckedWithdrawList(){
        List<Withdraw> withdrawList = adminService.getUnCheckedWithdrawList();
        CommonResponse res = new CommonResponse(0, "success", withdrawList);
        return res;
    }


    /**
     * =============================================================
     * @desc 同意用户充值
     * @author PandaClark
     * @date 2019/5/12 5:11 PM
     * @param rechargeId 充值记录ID
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员同意用户充值")
    @ResponseBody
    @RequestMapping( value = "/rechargeY", method = RequestMethod.POST )
    public CommonResponse acceptUserRecharge(HttpServletRequest request, @RequestParam("rechargeId") String rechargeId ){
        String email = (String) request.getAttribute("email");
        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = userService.getRechargeRecordById(rechargeId);
        if ( record == null){
            res.setData("没有该充值记录");
        } else if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.CheckUserRecharge");
            rechargeInfo.put("rechargeID",record.getPaymentId());
            // rechargeInfo.put("paymentid",record.getPaymentMethod());
            rechargeInfo.put("paymentid",record.getPaymentId());
            // fabric 发 post
            Map<String, String> result = adminFabricClient.CheckUserRechargeFabric(rechargeInfo);
            System.out.println(result);
            // 修改 mysql 字段
            // adminService.acceptUserRecharge(record.getPaymentId(), email);
            adminService.acceptUserRecharge(record, email);
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
    @ApiOperation(value = "管理员拒绝用户充值")
    @ResponseBody
    @RequestMapping( value = "/rechargeR", method = RequestMethod.POST )
    public CommonResponse rejectUserRecharge(HttpServletRequest request, @RequestParam("rechargeId") String rechargeId ){
        String email = (String) request.getAttribute("email");
        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Recharge record = userService.getRechargeRecordById(rechargeId);
        if ( record == null){
            res.setData("没有该充值记录");
        } else if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.RejectUserRecharge");
            rechargeInfo.put("rechargeID",record.getPaymentId());
            // 不需要这一句
            // rechargeInfo.put("paymentid",record.getPaymentId());
            // fabric 发 post
            Map<String, Object> result = adminFabricClient.RejectUserRechargeFabric(rechargeInfo);
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
     * @desc 同意机构用户提现
     * @author PandaClark
     * @date 2019/5/12 5:17 PM
     * @param withdrawId 提现记录的 payment_id
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员同意机构用户提现")
    @ResponseBody
    @RequestMapping( value = "/withdrawY", method = RequestMethod.POST )
    public CommonResponse acceptCompanyWithdraw(HttpServletRequest request, @RequestParam("withdrawId") String withdrawId){
        String email = (String) request.getAttribute("email");
        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Withdraw record = agencyService.getWithdrawRecordById(withdrawId);
        if ( record == null){
            res.setData("没有该提现记录");
        } else if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.CheckCompanyWithdraw");
            rechargeInfo.put("tokenWithdrawID",record.getPaymentId());
            rechargeInfo.put("paymentid",record.getPaymentMethod());
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
     * @desc 拒绝机构用户提现
     * @author PandaClark
     * @date 2019/5/12 5:18 PM
     * @param withdrawId 提现记录的 payment_id
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员拒绝机构用户提现")
    @ResponseBody
    @RequestMapping( value = "/withdrawR", method = RequestMethod.POST )
    public CommonResponse rejectCompanyWithdraw(HttpServletRequest request, @RequestParam("withdrawId") String withdrawId){
        String email = (String) request.getAttribute("email");
        CommonResponse res = new CommonResponse();
        // 先默认失败
        res.setStatus(1);
        res.setMessage("failed");
        // mysql 查出相关记录信息
        Withdraw record = agencyService.getWithdrawRecordById(withdrawId);
        if ( record == null){
            res.setData("没有改提现记录");
        } else if ( record.getIfChecked() != 0 ){
            res.setMessage("该记录已被审核过了！请勿重复审核");
        } else {
            Map<String, String> rechargeInfo = new HashMap<>();
            rechargeInfo.put("$class","org.education.RejectCompanyWithdraw");
            rechargeInfo.put("tokenWithdrawID",record.getPaymentId());
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
     * @author PandaClark
     * @date 2019/5/12 5:26 PM
     * =============================================================
     */
    @ApiOperation(value = "获取中央账户信息")
    @ResponseBody
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
    @ApiOperation(value = "获取权益分配合约")
    @ResponseBody
    @RequestMapping( value = "/contract", method = RequestMethod.GET )
    public CommonResponse contract(){
        CommonResponse res = new CommonResponse();

        List<ContractDto> infos = adminService.getContractInfo();
        res.setStatus(0);
        res.setMessage("success");
        res.setData(infos);
        return res;
    }


    /**
     * =============================================================
     * @desc 查看所有资源列表
     * @author PandaClark
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "查看所有资源列表", notes = "查看所有资源列表")
    @ResponseBody
    @RequestMapping( value = "/resourcelist", method = RequestMethod.GET )
    public CommonResponse resourceList(){
        List<FileInfo> files = fileService.getServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看待审核列表
     * @author PandaClark
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看待审核列表", notes = "管理员查看待审核列表")
    @ResponseBody
    @RequestMapping( value = "/resourcelistW", method = RequestMethod.GET )
    public CommonResponse resourceListW(){
        List<FileInfo> files = fileService.getUnCheckedServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看审核通过记录
     * @author PandaClark
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看审核通过记录", notes = "管理员查看审核通过记录")
    @ResponseBody
    @RequestMapping( value = "/resourcelistY", method = RequestMethod.GET )
    public CommonResponse resourceListY(){
        List<FileInfo> files = fileService.getCheckedServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }

    /**
     * =============================================================
     * @desc 管理员查看审核拒绝记录
     * @author PandaClark
     * @date 2019/5/16 1:53 PM
     * @return team.educoin.common.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员查看审核拒绝记录", notes = "管理员查看审核拒绝记录")
    @ResponseBody
    @RequestMapping( value = "/resourcelistR", method = RequestMethod.GET )
    public CommonResponse resourceListR(){
        List<FileInfo> files = fileService.getRejectServiceList();
        CommonResponse res = new CommonResponse(0, "success", files);
        return res;
    }


    /**
     * =============================================================
     * @desc 管理员审核通过资源
     * @author PandaClark
     * @date 2019/5/16 10:16 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员审核通过资源", notes = "管理员审核通过资源")
    @ResponseBody
    @RequestMapping( value = "/serviceY", method = RequestMethod.POST )
    public CommonResponse checkService(HttpServletRequest request, @RequestParam("id") String id ){

        CommonResponse res = null;
        String email = (String) request.getAttribute("email");

        try {
            FileInfo fileInfo = fileService.getFileInfoById(id);
            // 机构用户上传资源时，信息不上链，基本信息只存在数据库里，只有审核通过的资源才上链
            Map<String, Object> map = new HashMap<>();
            map.put("$class","org.education.RegisterService");
            map.put("serviceID", fileInfo.getId());
            map.put("serviceName", fileInfo.getFileTitle());
            map.put("readPrice", fileInfo.getFileReadPrice());
            map.put("ownershipPrice", fileInfo.getFileOwnerShipPrice());
            map.put("company", fileInfo.getFileInitialProvider());

            fileFabricClient.registerService(map);
            adminService.acceptService(email, id);
            res = new CommonResponse(0, "success", "注册新资源");
        } catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }

        return res;
    }

    /**
     * =============================================================
     * @desc 管理员审核拒绝资源
     * @author PandaClark
     * @date 2019/5/16 10:16 PM
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员审核拒绝资源", notes = "管理员审核拒绝资源")
    @ResponseBody
    @RequestMapping( value = "/serviceR", method = RequestMethod.POST )
    public CommonResponse rejectService( HttpServletRequest request,@RequestParam("id") String id ){
        String email = (String) request.getAttribute("email");
        adminService.rejectService(email, id);
        CommonResponse res = new CommonResponse(0, "success", "已审核拒绝");
        return res;
    }


    /**
     * =============================================================
     * @desc 管理员处理资源侵权，通过资源 ID 获取水印
     * @author Messi-Q ; Modified by PandaClark
     * @date 2019/6/24 3:10 PM
     * @param id
     * @return CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "管理员通过资源ID获取水印，处理资源侵权", notes = "管理员提取资源中的水印信息")
    @ResponseBody
    @RequestMapping(value = "/extractWatermarkInfo/{id}", method = RequestMethod.GET)
    public CommonResponse extractWatermarkInfoById(@PathVariable("id") String id) {
        CommonResponse res;

        // 根据文件id获取文件名
        FileInfo fileInfo = fileService.getFileInfoById(id);
        String filename = fileInfo.getFileName();

        try {
            // 获得水印
            String watermark = WatermarkUtil.extractWatermark(filename, 0);
            res = new CommonResponse(0, "success", "watermark:" + watermark);
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }

        return res;
    }



    /**
     * =============================================================
     * @desc 管理员处理资源侵权，提取上传资源中的水印
     * @author PandaClark
     * @date 2019/10/28 2:24 PM
     * @param
     * @return
     * =============================================================
     */
    @ApiOperation(value = "管理员通过提取上传文件的水印，处理资源侵权", notes = "管理员提取资源中的水印信息")
    @ResponseBody
    @RequestMapping(value = "/extractWatermarkInfo", method = RequestMethod.POST)
    public CommonResponse extractWatermarkInfoByPost(@RequestParam MultipartFile file, @RequestParam String id) throws IOException {
        CommonResponse res;

        String filename = file.getOriginalFilename();
        // 文件上传操作
        Files.copy(file.getInputStream(), Paths.get(FileUtil.EXTRACT_UPLOAD_DIR,filename), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("filename: " + filename);

        try {
            // 获得水印
            String watermark = WatermarkUtil.extractWatermark(filename, 0);
            Map<String, Object> result = new HashMap<>();
            result.put("watermark", watermark);
            FileInfo fileInfo = fileService.getFileInfoById(id);
            result.put("fileInfo", fileInfo);
            res = new CommonResponse(0, "success", result);
        } catch (Exception e) {
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }

        return res;
    }


    /**
     * =============================================================
     * @desc 水印嵌入和提取的测试接口
     * @author PandaClark
     * @date 2019/10/28 7:32 PM
     * @param
     * @return 嵌入水印的内容
     * =============================================================
     */
    @ApiOperation(value = "添加水印脚本测试")
    @ResponseBody
    @RequestMapping(value = "/testWatermarkEmbed", method = RequestMethod.POST)
    public String testWatermark1(@RequestParam MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        // 下载的时候嵌入水印
        Files.copy(file.getInputStream(), Paths.get(FileUtil.TEST_EXTRACT_UPLOAD_DIR,filename), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("filename: " + filename);

        String watermark = null;
        try {
            // 获得水印
            // watermark = WatermarkUtil.embedWatermark(filename,"file-owner:thinpanda","downloader:zju-incas");
            watermark = WatermarkUtil.embedWatermark("资源id", filename,"ZjuEducation@zju.edu.cn","thinpanda", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return watermark;

    }

    /**
     * =============================================================
     * @desc 水印嵌入或提取的测试接口
     * @author PandaClark
     * @date 2019/10/28 7:36 PM
     * @param
     * @return 从水印提取出的内容
     * =============================================================
     */
    @ApiOperation(value = "提取水印脚本测试")
    @ResponseBody
    @RequestMapping(value = "/testWatermarkExtract", method = RequestMethod.POST)
    public String testWatermark2(@RequestParam MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        // 文件上传操作
        // 上传的是已经被嵌入水印的图片
        Files.copy(file.getInputStream(), Paths.get(FileUtil.TEST_EXTRACT_OUT_DIR,filename), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("filename: " + filename);

        String watermark = null;
        try {
            // 获得水印
            watermark = WatermarkUtil.extractWatermark(filename, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return watermark;

    }


    /**
     * =============================================================
     * @desc 获取未审核的侵权上诉记录
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取未审核的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appealList/unchecked", method = RequestMethod.GET)
    public CommonResponse getUncheckedAppealList(){
        CommonResponse res = null;
        List<Appeal> list = null;
        try {
            list = appealService.getUncheckedAppealList();
            res = new CommonResponse(0, "success", list);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }


    /**
     * =============================================================
     * @desc 获取已审核的侵权上诉记录
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取已审核的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appealList/checked", method = RequestMethod.GET)
    public CommonResponse getCheckedAppealList(){
        CommonResponse res = null;
        List<Appeal> listY = null;
        List<Appeal> listR = null;
        Map<String, List> map = new HashMap();
        try {
            listY = appealService.getApprovedAppealList();
            listR = appealService.getRejectAppealList();
            map.put("approved", listY);
            map.put("reject", listR);
            res = new CommonResponse(0, "success", map);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }


    /**
     * =============================================================
     * @desc 获取审核通过的侵权上诉记录
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取审核通过的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appealList/approved", method = RequestMethod.GET)
    public CommonResponse getApprovedAppealList(){
        CommonResponse res = null;
        List<Appeal> list = null;
        try {
            list = appealService.getApprovedAppealList();
            res = new CommonResponse(0, "success", list);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }

    /**
     * =============================================================
     * @desc 获取审核拒绝的侵权上诉记录
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取审核拒绝的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appealList/reject", method = RequestMethod.GET)
    public CommonResponse getRejectAppealList(){
        CommonResponse res = null;
        List<Appeal> list = null;
        try {
            list = appealService.getRejectAppealList();
            res = new CommonResponse(0, "success", list);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }


    @ApiOperation(value = "测试接口")
    @ResponseBody
    @RequestMapping(value = "/justTestJavaInvokePython", method = RequestMethod.GET)
    public String justTest() throws IOException {

        Path watermark = Paths.get("watermark").toAbsolutePath().normalize();
        try {
            if (!Files.exists(watermark)){
                Files.createDirectory(watermark);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageWaterMarkEmbedTool = ResourceUtils.getURL(watermark + "/image_watermark_embed.py").getPath();

        System.out.println(imageWaterMarkEmbedTool);

        return imageWaterMarkEmbedTool;
    }


    /**
     * =============================================================
     * @desc 管理员同意侵权上诉
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取审核拒绝的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appeal/approve", method = RequestMethod.POST)
    public CommonResponse approveAppeal(HttpServletRequest request, @RequestParam("id") String id){
        CommonResponse res = null;
        String email = (String) request.getAttribute("email");
        try {
            boolean update = appealService.approveAppeal(id, email);
            res = new CommonResponse(0, "success", update);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }


    /**
     * =============================================================
     * @desc 管理员拒绝侵权上诉
     * @author PandaClark
     * @date 2019/12/15 6:47 PM
     * @param
     * @return team.educoin.transaction.controller.CommonResponse
     * =============================================================
     */
    @ApiOperation(value = "获取审核拒绝的侵权上诉记录")
    @ResponseBody
    @RequestMapping(value = "/appeal/reject", method = RequestMethod.POST)
    public CommonResponse rejectAppeal(HttpServletRequest request, @RequestParam("id") String id){
        CommonResponse res = null;
        String email = (String) request.getAttribute("email");
        try {
            boolean update = appealService.rejectAppeal(id, email);
            res = new CommonResponse(0, "success", update);
        }catch (Exception e){
            e.printStackTrace();
            res = new CommonResponse(1, "failed", e.getMessage());
        }
        return res;
    }

}



















