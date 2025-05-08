package com.allieat.service;

import com.allieat.dto.AdminDTO;
import java.util.Map;

public interface BackStageLoginService {

    Map<String, Object> findByAccount(AdminDTO admin);
    Map<String, Object> logout(String token);
}
