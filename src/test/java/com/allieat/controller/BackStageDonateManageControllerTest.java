package com.allieat.controller;


import com.allieat.dto.DonateDTO;
import com.allieat.dto.DonateInitResponse;
import com.allieat.dto.DonationDetailsDTO;
import com.allieat.service.BackStageDonateManageService;
import com.allieat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BackStageDonateManageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BackStageDonateManageService backStageDonateManageService;

    //攔截器pass設定
    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
    }

    @Test
    @DisplayName("取得初始資料成功")
    void getInitDataSuccess() throws Exception {
        DonateInitResponse initResponse = new DonateInitResponse();
        DonateDTO fakeDTO = new DonateDTO();
        List<DonateDTO> fakeDTOList = new ArrayList<>();
        fakeDTOList.add(fakeDTO);
        initResponse.setDonationList(fakeDTOList);

        ResponseEntity<DonateInitResponse> mockResponse = ResponseEntity.ok().body(initResponse);

        Mockito.when(backStageDonateManageService.getInitData()).thenReturn(mockResponse.getBody());

        mockMvc.perform(MockMvcRequestBuilders.get("/backStage/donaManage")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donationList").isArray());


    }

    @Test
    @DisplayName("取得初始資料失敗")
    void getInitDataFailure() throws Exception {

        Mockito.when(backStageDonateManageService.getInitData())
                .thenThrow(new RuntimeException("Something went wrong"));


        mockMvc.perform(MockMvcRequestBuilders.get("/backStage/donaManage")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errormessage").value("服務異常請稍後再試"));
    }

    @Test
    @DisplayName("查詢資料成功")
    void getDonateRecordSuccess() throws Exception {
        DonationDetailsDTO dto = new DonationDetailsDTO();
        dto.setDonorName("假資料");
        dto.setIdentityNumber("A123456789");
        dto.setIdentityType(true);
        dto.setDonationAmount(500);
        dto.setDonationTime(new Timestamp(System.currentTimeMillis()));
        dto.setContactEmail("test@ABC.com");
        dto.setContactPhone("0987654321");
        dto.setAddress("台北市大安區");

        Mockito.when(backStageDonateManageService.getDonateRecord(1)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/backStage/donaRecord")
                        .param("id", "1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donorName").value("假資料"))
                .andExpect(jsonPath("$.identityNumber").value("A123456789"))
                .andExpect(jsonPath("$.identityType").value(true))
                .andExpect(jsonPath("$.donationAmount").value(500))
                .andExpect(jsonPath("$.contactEmail").value("test@ABC.com"))
                .andExpect(jsonPath("$.address").value("台北市大安區"));
    }

    @Test
    @DisplayName("查詢資料失敗")
    void getDonateFailure() throws Exception {
        Mockito.when(backStageDonateManageService.getDonateRecord(99999))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/backStage/donaRecord")
                        .param("id", "99999")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorMsg").value("查無資料，請重新確認"));

    }

}






