package team.educoin.transaction.pojo;

import java.util.Date;

/**
 * @description: 机构用户资源侵权申诉
 * @author: PandaClark
 * @create: 2019-11-03
 */
public class Appeal {

    private String id;
    private String agencyEmail;
    private String fileId;
    private String fileName;
    private String detail;
    private String adminEmail;
    private String watermark;
    private int ifChecked;
    private Date checkTime;
    private Date createTime;
    private Date updateTime;

    public Appeal() {
    }

    public Appeal(String id, String agencyEmail, String fileId, String fileName, String detail, int ifChecked) {
        this.id = id;
        this.agencyEmail = agencyEmail;
        this.fileId = fileId;
        this.fileName = fileName;
        this.detail = detail;
        this.ifChecked = ifChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyEmail() {
        return agencyEmail;
    }

    public void setAgencyEmail(String agencyEmail) {
        this.agencyEmail = agencyEmail;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public int getIfChecked() {
        return ifChecked;
    }

    public void setIfChecked(int ifChecked) {
        this.ifChecked = ifChecked;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "id='" + id + '\'' +
                ", agencyEmail='" + agencyEmail + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", detail='" + detail + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", watermark='" + watermark + '\'' +
                ", ifChecked=" + ifChecked +
                ", checkTime=" + checkTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
