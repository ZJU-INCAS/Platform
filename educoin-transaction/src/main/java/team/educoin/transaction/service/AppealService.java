package team.educoin.transaction.service;

import team.educoin.transaction.pojo.Appeal;

import java.util.List;

/**
 * @description: 侵权上诉服务接口
 * @author: PandaClark
 * @create: 2019-12-07
 */
public interface AppealService {

    /**
     * =============================================================
     * @desc  注册记录机构用户的上诉请求
     * @author PandaClark
     * @date 2019/12/7 2:24 PM
     * @param appeal
     * @return boolean
     * =============================================================
     */
    boolean registerAppeal(Appeal appeal);


    /**
     * =============================================================
     * @desc 根据机构用户id查询对应的上诉记录
     * @author PandaClark
     * @date 2019/12/15 3:20 PM
     * @param agency
     * @return List<Appeal>
     * =============================================================
     */

    List<Appeal> getAgencyAppealList(String agency);


    /**
     * =============================================================
     * @desc 根据审核结果获取机构用户申诉请求记录
     * @author PandaClark
     * @date 2019/12/15 6:39 PM
     * @param
     * @return
     * =============================================================
     */
    List<Appeal> getUncheckedAppealList();


    /**
     * =============================================================
     * @desc 根据审核结果获取机构用户申诉请求记录
     * @author PandaClark
     * @date 2019/12/15 6:39 PM
     * @param
     * @return
     * =============================================================
     */
    List<Appeal> getApprovedAppealList();


    /**
     * =============================================================
     * @desc 根据审核结果获取机构用户申诉请求记录
     * @author PandaClark
     * @date 2019/12/15 6:39 PM
     * @param
     * @return
     * =============================================================
     */
    List<Appeal> getRejectAppealList();

    /**
     * =============================================================
     * @desc 管理员审核通过申诉请求
     * @author PandaClark
     * @date 2019/12/15 8:59 PM
     * @param id
     * @return void
     * =============================================================
     */
    boolean approveAppeal(String id, String admin);

    /**
     * =============================================================
     * @desc 管理员审核拒绝申诉请求
     * @author PandaClark
     * @date 2019/12/15 8:59 PM
     * @param id
     * @return void
     * =============================================================
     */
    boolean rejectAppeal(String id, String admin);
}
