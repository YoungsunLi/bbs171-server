package net.lsun.bbs171.entity;

import lombok.Data;

@Data
public class RegUser {
    private String phone;
    private String username;
    private String password;
    private String gender;
    private String Code;
}
