package com.allieat.dto;

public class AdminDTO {
    private String account;
    private String password;

    public AdminDTO(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public AdminDTO() {
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}