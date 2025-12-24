package com.baby.main.secure.basic;

public class UserDto {
    // 数据库id
    private Integer id;
    // 用户账户
    private String account;
    // 用户名
    private String name;
    // 用户密码
    private String password;
    // 用户邮箱
    private String email;
    // 用户手机号
    private String telephone;
    // 密码有效期
    private Long passwordValidDate;
    // 账户状态
    private Integer status;
    // 登陆失败次数
    private Integer loginFailCount;
    // 历史密码
    private String passwordLog;
    // 修订
    private Integer revision;
    // 用户id
    private String uuid;
    // 用户是否锁定
    private Integer locked;
    // 用户是否合法
    private Integer valid;
    // 用户扩展信息
    private String extendInfo;
    // 账号有效期
    private Long accountValidData;
    // 用户头像
    private String image;
    // 电子签名密码有效期
    private Long signaturePwdValidDate;
    // 电子签名历史密码
    private String signaturePwdLog;
    // 用户详情
    private String userDetail;

    public UserDto() {
    }

    public UserDto(Integer id, String account, String name, String password, String email, String telephone, Long passwordValidDate, Integer status, Integer loginFailCount, String passwordLog, Integer revision, String uuid, Integer locked, Integer valid, String extendInfo, Long accountValidData, String image, Long signaturePwdValidDate, String signaturePwdLog, String userDetail) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.passwordValidDate = passwordValidDate;
        this.status = status;
        this.loginFailCount = loginFailCount;
        this.passwordLog = passwordLog;
        this.revision = revision;
        this.uuid = uuid;
        this.locked = locked;
        this.valid = valid;
        this.extendInfo = extendInfo;
        this.accountValidData = accountValidData;
        this.image = image;
        this.signaturePwdValidDate = signaturePwdValidDate;
        this.signaturePwdLog = signaturePwdLog;
        this.userDetail = userDetail;
    }

    public Integer getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Long getPasswordValidDate() {
        return passwordValidDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPasswordValidDate(Long passwordValidDate) {
        this.passwordValidDate = passwordValidDate;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setLoginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public void setPasswordLog(String passwordLog) {
        this.passwordLog = passwordLog;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public void setAccountValidData(Long accountValidData) {
        this.accountValidData = accountValidData;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSignaturePwdValidDate(Long signaturePwdValidDate) {
        this.signaturePwdValidDate = signaturePwdValidDate;
    }

    public void setSignaturePwdLog(String signaturePwdLog) {
        this.signaturePwdLog = signaturePwdLog;
    }

    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    public String getPasswordLog() {
        return passwordLog;
    }

    public Integer getRevision() {
        return revision;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getLocked() {
        return locked;
    }

    public Integer getValid() {
        return valid;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public Long getAccountValidData() {
        return accountValidData;
    }

    public String getImage() {
        return image;
    }

    public Long getSignaturePwdValidDate() {
        return signaturePwdValidDate;
    }

    public String getSignaturePwdLog() {
        return signaturePwdLog;
    }

    public String getUserDetail() {
        return userDetail;
    }
}
