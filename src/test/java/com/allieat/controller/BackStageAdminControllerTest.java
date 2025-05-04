package com.allieat.controller;

import com.allieat.dto.ChangePasswordDTO;
import com.allieat.dto.NewAdminDTO;
import com.allieat.service.BackStageAdminService;
import com.allieat.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BackStageAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //springboot 3.4.x版本 使用 @MockitoBean取代@MockBean
    @MockitoBean
    private BackStageAdminService backStageAdminService;
    @MockitoBean
    private JwtUtil jwtUtil;

    //攔截器pass設定
    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
    }

    @Test
    @DisplayName("建立新管理者成功")
    void testNewAdminSuccess() throws Exception {
        //傳入假的使用者dto資訊
        NewAdminDTO inputDto = new NewAdminDTO();
        inputDto.setAccount("admin");
        inputDto.setPassword("123456");
        //模擬 service層回傳結果-創建管理者成功
        NewAdminDTO mockResponse = new NewAdminDTO();
        mockResponse.setStatus("success");

        Mockito.when(backStageAdminService.newAdmin(any(NewAdminDTO.class)))
                .thenReturn(mockResponse);

        //POST請求並驗證回傳狀態與 JSON 內容-新使用者創建成功
        MockHttpServletRequestBuilder request = post("/backStage/admin")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("建立新管理者失敗")
    void testNewAdminFailure() throws Exception {
        //傳入假的使用者dto資訊
        NewAdminDTO inputDto = new NewAdminDTO();
        inputDto.setAccount("admin");
        inputDto.setPassword("123456");
        //模擬 service層回傳結果-創建管理者失敗
        NewAdminDTO mockResponse = new NewAdminDTO();
        mockResponse.setStatus("fail");

        Mockito.when(backStageAdminService.newAdmin(any(NewAdminDTO.class)))
                .thenReturn(mockResponse);

        //POST請求並驗證回傳狀態與 JSON 內容-新使用者創建失敗
        MockHttpServletRequestBuilder request = post("/backStage/admin")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("fail"));
    }

    @Test
    @DisplayName("建立新管理者-資訊為空值")
    void testNewAdminValidationError() throws Exception {
        NewAdminDTO invalidDto = new NewAdminDTO(); //驗證DTO @NotNull
        //POST請求並驗證回傳狀態與 JSON 內容-新使用者資訊空值
        MockHttpServletRequestBuilder request = post("/backStage/admin")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("更改密碼成功")
    void testChangePasswordSuccess() throws Exception {
        //傳入假的更改密碼的dto資訊
        ChangePasswordDTO validDto = new ChangePasswordDTO();
        validDto.setAccount("admin");
        validDto.setOldPassword("old123");
        validDto.setNewPassword("new123");

        //模擬 service層回傳結果-成功
        ChangePasswordDTO mockResponse = new ChangePasswordDTO();
        mockResponse.setStatus("success");

        Mockito.when(backStageAdminService.changePassword(any(ChangePasswordDTO.class)))
                .thenReturn(mockResponse);

        //PUT請求並驗證回傳狀態與 JSON 內容
        MockHttpServletRequestBuilder request = put("/backStage/admin/changePassword")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("修改密碼失敗")
    void testChangePasswordFailure() throws Exception {
        //傳入假的更改密碼的dto資訊
        ChangePasswordDTO validDto = new ChangePasswordDTO();
        validDto.setAccount("admin");
        validDto.setOldPassword("old123");
        validDto.setNewPassword("new123");

        //設定service層回傳結果-失敗
        Mockito.when(backStageAdminService.changePassword(any(ChangePasswordDTO.class)))
                .thenThrow(new RuntimeException("fail"));

        //PUT請求並驗證回傳狀態與 JSON 內容
        MockHttpServletRequestBuilder request = put("/backStage/admin/changePassword")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto));

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"));
    }
}
