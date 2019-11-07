package team.educoin.transaction.dto.integration;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 用户id、用户名称、资源id、交易id、交易学豆、时间戳
 * @author: PandaClark
 * @create: 2019-09-20
 */

public class TransactionDto implements Serializable {
    private String userId;
    private String username;
    private String serviceId;
    private String serviceName;
    private String txId;
    private Double courseBean;
    private Date timestamp;

    public TransactionDto() {
    }

    public TransactionDto(String userId, String username, String serviceId, String serviceName, String txId, Double courseBean, Date timestamp) {
        this.userId = userId;
        this.username = username;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.txId = txId;
        this.courseBean = courseBean;
        this.timestamp = timestamp;
    }

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

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txID) {
        this.txId = txID;
    }

    public Double getCourseBean() {
        return courseBean;
    }

    public void setCourseBean(Double courseBean) {
        this.courseBean = courseBean;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", txId='" + txId + '\'' +
                ", courseBean=" + courseBean +
                ", timestamp=" + timestamp +
                '}';
    }
}
