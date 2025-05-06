package com.allieat.controller;

import com.allieat.constant.OrgManageConstant;
import com.allieat.dto.UpdateOrgDTO;
import com.allieat.service.BackStageOrgManageService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.allieat.constant.OrgManageConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BackStageOrgManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BackStageOrgManageService backStageOrgManageService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any())).thenReturn(true);
    }

    @Test
    @DisplayName("取得初始資料成功")
    void getInitData_success() throws Exception {
        Mockito.when(backStageOrgManageService.getInitData())
                .thenReturn(Map.of("orgInitDataList", List.of("A", "B")));

        mockMvc.perform(get("/backStage/orgManage")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orgInitDataList[0]").value("A"));
    }

    @Test
    @DisplayName("取得初始資料失敗")
    void getInitData_failure() throws Exception {
        Mockito.when(backStageOrgManageService.getInitData())
                .thenThrow(new RuntimeException("連線異常"));

        mockMvc.perform(get("/backStage/orgManage")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value(ORG_ERROR));
    }

    @Test
    @DisplayName("更新資料成功")
    void updateInit_success() throws Exception {
        Mockito.when(backStageOrgManageService.getUpdateInitData(1))
                .thenReturn(Map.of("message", "ok", "organizationId", 1));

        mockMvc.perform(get("/backStage/orgManage/updateInit?id=1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value(1));
    }

    @Test
    @DisplayName("缺少ID")
    void updateInit_idMissing() throws Exception {
        Mockito.when(backStageOrgManageService.getUpdateInitData(1))
                .thenReturn(Map.of("message", ORG_ID_MISSING));

        mockMvc.perform(get("/backStage/orgManage/updateInit?id=1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ORG_ID_MISSING));
    }

    @Test
    @DisplayName("查無資料無法更新")
    void updateInit_notFound() throws Exception {
        Mockito.when(backStageOrgManageService.getUpdateInitData(1))
                .thenReturn(Map.of("message", ORG_NOT_FOUND));

        mockMvc.perform(get("/backStage/orgManage/updateInit?id=1")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ORG_NOT_FOUND));
    }

    @Test
    @DisplayName("更新成功")
    void updateData_success() throws Exception {
        UpdateOrgDTO dto = new UpdateOrgDTO();
        dto.setOrganizationId(1);
        dto.setName("Updated Org");

        Mockito.when(backStageOrgManageService.updateOrgData(any()))
                .thenReturn(Map.of(
                        "organizationId", 1,
                        "name", "Updated Org"   ,
                        "type", "A",
                        "status", "Active",
                        "createdTime", Timestamp.valueOf(LocalDateTime.now()),
                        "success", true));

        mockMvc.perform(put("/backStage/orgManage/updateData")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.name").value("Updated Org"));
    }

    @Test
    @DisplayName("更新失敗，缺少id")
    void updateData_idMissing() throws Exception {
        Mockito.when(backStageOrgManageService.updateOrgData(any()))
                .thenReturn(Map.of("message", ORG_ID_MISSING));

        mockMvc.perform(put("/backStage/orgManage/updateData")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"No ID Org\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ORG_ID_MISSING));
    }

    @Test
    @DisplayName("更新失敗，找不到資料")
    void updateData_notFound() throws Exception {
        Mockito.when(backStageOrgManageService.updateOrgData(any()))
                .thenReturn(Map.of("message", ORG_NOT_FOUND));

        mockMvc.perform(put("/backStage/orgManage/updateData")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizationId\": 999, \"name\": \"Unknown\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ORG_NOT_FOUND));
    }

    @Test
    @DisplayName("創建新組織資料成功")
    void newOrgData_success() throws Exception {
        UpdateOrgDTO dto = new UpdateOrgDTO();
        dto.setName("New Org");

        Mockito.when(backStageOrgManageService.newOrgData(any()))
                .thenReturn(Map.of(
                        "organizationId", 10,
                        "name", "New Org",
                        "type", "B",
                        "status", "Active",
                        "createdTime", Timestamp.valueOf(LocalDateTime.now()),
                        "success", true));

        mockMvc.perform(put("/backStage/orgManage/newOrgData")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.name").value("New Org"));
    }

    @Test
    @DisplayName("組織名稱重複")
    void newOrgData_duplicate() throws Exception {
        UpdateOrgDTO dto = new UpdateOrgDTO();
        dto.setName("Existing Org");

        Mockito.when(backStageOrgManageService.newOrgData(any()))
                .thenReturn(Map.of("error", ORG_DUPLICATE));

        mockMvc.perform(put("/backStage/orgManage/newOrgData")
                        .header("Authorization", "Bearer faketoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(ORG_DUPLICATE));
    }
}
