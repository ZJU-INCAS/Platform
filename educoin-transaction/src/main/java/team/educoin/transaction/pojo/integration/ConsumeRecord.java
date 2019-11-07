package team.educoin.transaction.pojo.integration;

import java.util.Date;

/**
 * @description:
 * @author: PandaClark
 * @create: 2019-10-08
 */
public class ConsumeRecord {
    String userId;
    String username;
    String serviceId;
    String serviceName;
    Double courseBean;
    String txID;
    String transactionId;
    Date createTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getCourseBean() {
        return courseBean;
    }

    public void setCourseBean(Double courseBean) {
        this.courseBean = courseBean;
    }

    public String getTxID() {
        return txID;
    }

    public void setTxID(String txID) {
        this.txID = txID;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
