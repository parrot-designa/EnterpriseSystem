package com.enterprisesystem.babysecure.model.dto;
import lombok.Data;

@Data
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
}