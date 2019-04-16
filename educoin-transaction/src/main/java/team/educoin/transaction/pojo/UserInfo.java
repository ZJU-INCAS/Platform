package team.educoin.transaction.pojo;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
public class UserInfo {
    private String id;
    private String email;
    private String accountBalance;

    public UserInfo() {
    }

    public UserInfo(String id, String email, String accountBalance) {
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

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
