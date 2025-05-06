package com.allieat.scheduler;

import org.springframework.stereotype.Service;

@Service
public interface DonationUpdateNotifier {
    void checkForUpdate();
}
