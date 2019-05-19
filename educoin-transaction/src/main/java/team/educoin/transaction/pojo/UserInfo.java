package team.educoin.transaction.pojo;

/**
 *  存放普通用户、机构用户、管理员余额
 */
public class UserInfo {
    private String id;
    private String email;
    private Double accountBalance;

    public UserInfo() {
    }

    public UserInfo(String id, String email, Double accountBalance) {
        this.id = id;
        this.email = email;
        this.accountBalance = accountBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
