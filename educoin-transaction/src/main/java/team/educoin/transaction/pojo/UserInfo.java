package team.educoin.transaction.pojo;

public class UserInfo {
    private String id;
    private String email;
    private String password;
    private String fringerprint;
    private String iris;
    private String qq;
    private String identityCard;
    private String buyerType;
    private Integer age;
    private String sexual;
    private String educationLevel;
    private String address;
    private Double accountBalance;
    private String bankAccount;


    public UserInfo() {
    }

    public UserInfo(String id, String email, Double accountBalance) {
        this.id = id;
        this.email = email;
        this.accountBalance = accountBalance;
    }

    public UserInfo(String id, String email, String password, String fringerprint, String iris,
                    String qq, String identityCard, String buyerType, Integer age, String sexual,
                    String educationLevel, String address, Double accountBalance, String bankAccount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fringerprint = fringerprint;
        this.iris = iris;
        this.qq = qq;
        this.identityCard = identityCard;
        this.buyerType = buyerType;
        this.age = age;
        this.sexual = sexual;
        this.educationLevel = educationLevel;
        this.address = address;
        this.accountBalance = accountBalance;
        this.bankAccount = bankAccount;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFringerprint() {
        return fringerprint;
    }

    public void setFringerprint(String fringerprint) {
        this.fringerprint = fringerprint;
    }

    public String getIris() {
        return iris;
    }

    public void setIris(String iris) {
        this.iris = iris;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(String buyerType) {
        this.buyerType = buyerType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSexual() {
        return sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
