package team.educoin.transaction.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel( value = "管理员信息" )
public class AdminInfo {


    private String email;
    private String password;
    @ApiModelProperty( hidden = true )
    private String fingerprint;
    @ApiModelProperty( hidden = true )
    private String iris;
    @ApiModelProperty( hidden = true )
    private Double accountBalance;
    private String bankAccount;


    public AdminInfo() {
    }


    public AdminInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public AdminInfo(String email){
        this.email = email;

    }
    public AdminInfo(String email, String password, String fingerprint, String iris,
                     Double accountBalance, String bankAccount) {

        this.email = email;
        this.password = password;
        this.fingerprint = fingerprint;
        this.iris = iris;
        this.accountBalance = accountBalance;
        this.bankAccount = bankAccount;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getIris() {
        return iris;
    }

    public void setIris(String iris) {
        this.iris = iris;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}
