package com.allieat.controller;

import com.allieat.service.BackStageAuditOrgService;
import com.allieat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//避免取資料
@TestPropertySource(properties = {
        "spring.web.resources.add-mappings=false"
})
public class BackStageAuditOrgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //springboot 3.4.x版本 使用 @MockitoBean取代@MockBean
    @MockitoBean
    private BackStageAuditOrgService backStageAuditOrgService;

    @MockitoBean
    private JwtUtil jwtUtil;

    //攔截器pass設定
    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
    }

    @Test
    @DisplayName("取得單位素材")
    void testGetAuditImgSuccess() throws Exception{
        //假資料
        byte[] imgTest = "fake image".getBytes(StandardCharsets.UTF_8);
        //假請求
        ResponseEntity<byte[]> mockResponse = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imgTest);

        Mockito.when(backStageAuditOrgService.getImgByOrgId(1)).thenReturn(mockResponse);

        //GET請求取圖
        mockMvc.perform(MockMvcRequestBuilders.get("/backStage/orgAudit/image")
                        .param("orgId", "1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imgTest));
    }

}
