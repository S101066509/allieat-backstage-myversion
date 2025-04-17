package com.allieat.service;

import com.allieat.dto.DonateInitResponse;
import com.allieat.dto.DonationDetailsDTO;

public interface BackStageDonateManageService {
    DonateInitResponse getInitData();
    DonationDetailsDTO getDonateRecord(Integer id);
}
