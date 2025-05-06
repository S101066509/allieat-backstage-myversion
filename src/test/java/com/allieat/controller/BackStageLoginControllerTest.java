package com.allieat.controller;


import com.allieat.constant.LoginConstant;
import com.allieat.service.BackStageLoginService;
import com.allieat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BackStageLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BackStageLoginService backStageLoginService;

    //攔截器pass設定
    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
    }

    @Test
    @DisplayName("登入成功")
    void loginSuccess() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("loginState", LoginConstant.SUCCESS);


        Mockito.when(backStageLoginService.findByAccount(any())).thenReturn(mockResult);

        mockMvc.perform(post("/backStage/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": \"admin\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginState").value(LoginConstant.SUCCESS));

    }

    @Test
    @DisplayName("密碼錯誤")
    void loginPasswordIncorrect() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("loginState", LoginConstant.PASSWORD_INCORRECT);


        Mockito.when(backStageLoginService.findByAccount(any())).thenReturn(mockResult);

        mockMvc.perform(post("/backStage/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": \"admin\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginState").value(LoginConstant.PASSWORD_INCORRECT));

    }


    @Test
    @DisplayName("查無帳號")
    void loginAccountNotExist() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("loginState", LoginConstant.ACCOUNT_NOT_EXIST);

        Mockito.when(backStageLoginService.findByAccount(any())).thenReturn(mockResult);

        mockMvc.perform(post("/backStage/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": \"admin\", \"password\": \"password\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.loginState").value(LoginConstant.ACCOUNT_NOT_EXIST));
    }

    @Test
    @DisplayName("資料庫連線異常")
    void loginUnexpectedError() throws Exception {
        Mockito.when(backStageLoginService.findByAccount(any())).thenThrow(new RuntimeException("資料庫連線異常"));

        mockMvc.perform(post("/backStage/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": \"admin\", \"password\": \"password\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.loginState").value(LoginConstant.SERVER_ERROR));
    }

}
