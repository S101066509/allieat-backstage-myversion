package com.allieat.scheduler;


import com.allieat.service.impl.BackStageHomePageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/*
  *用於排程固定10秒去確認資料
  */
@Component
public class HomePageUpdateScheduler {

    @Autowired
    private BackStageHomePageServiceImpl backStageHomePageServiceImpl;
    
    @Scheduled(fixedDelay = 10000)
    public void checkDonation() {
    	backStageHomePageServiceImpl.checkForUpdate();
    }
}