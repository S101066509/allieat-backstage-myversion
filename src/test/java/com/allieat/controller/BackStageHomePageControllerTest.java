package com.allieat.controller;


import com.allieat.scheduler.DonationUpdateNotifier;
import com.allieat.service.BackStageHomePageService;
import com.allieat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BackStageHomePageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private BackStageHomePageService backStageHomePageService;//service
    @MockitoBean
    private DonationUpdateNotifier donationUpdateNotifier;//排程器的service

    //攔截器pass設定
    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtil.validateToken(any(String.class))).thenReturn(true);
    }
    @Test
    void totalDonationsSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("totalDonations", 10000);
        Mockito.when(backStageHomePageService.getTotalDonations()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/totalDonations")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDonations").value(10000));
    }

    @Test
    void totalDonorsSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("totalDonors", 500);
        Mockito.when(backStageHomePageService.getTotalDonors()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/totalDonors")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDonors").value(500));
    }

    @Test
    void monthlyDonationsSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("monthlyDonations", 3000);
        Mockito.when(backStageHomePageService.getMonthlyDonations()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/monthlyDonations")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyDonations").value(3000));
    }

    @Test
    void newDonorsSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("newDonors", 20);
        Mockito.when(backStageHomePageService.getNewDonors()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/newDonors")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newDonors").value(20));
    }

    @Test
    void donationChartSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("donationChart", Map.of("labels"
                                                                    ,List.of("Jan")
                                                                    , "data", List.of(1000)));

        Mockito.when(backStageHomePageService.getDonationChart()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/donationChart")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donationChart.labels[0]").value("Jan"))
                .andExpect(jsonPath("$.donationChart.data[0]").value(1000));
    }

    @Test
    void usageChartSuccess() throws Exception {
        Map<String, Object> mockResult = Map.of("usageChart", Map.of("labels"
                                                                , List.of("Jan")
                                                                , "data"
                                                                , List.of(30)));
        Mockito.when(backStageHomePageService.getUsageChart()).thenReturn(mockResult);

        mockMvc.perform(get("/backStage/homePage/usageChart")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usageChart.labels[0]").value("Jan"))
                .andExpect(jsonPath("$.usageChart.data[0]").value(30));
    }





    @Test
    void totalDonationsFailure() throws Exception {
        Mockito.when(backStageHomePageService.getTotalDonations()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/totalDonations")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }

    @Test
    void totalDonorsFailure() throws Exception {
        Mockito.when(backStageHomePageService.getTotalDonors()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/totalDonors")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }

    @Test
    void monthlyDonationsFailure() throws Exception {
        Mockito.when(backStageHomePageService.getMonthlyDonations()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/monthlyDonations")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }

    @Test
    void newDonorsFailure() throws Exception {
        Mockito.when(backStageHomePageService.getNewDonors()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/newDonors")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }

    @Test
    void donationChartFailure() throws Exception {
        Mockito.when(backStageHomePageService.getDonationChart()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/donationChart")
                        .header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }

    @Test
    void usageChartFailure() throws Exception {
        Mockito.when(backStageHomePageService.getUsageChart()).thenThrow(new RuntimeException("資料取得失敗"));

        mockMvc.perform(get("/backStage/homePage/usageChart").
                        header("Authorization", "Bearer faketoken"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("伺服器內部錯誤"));
    }


}
