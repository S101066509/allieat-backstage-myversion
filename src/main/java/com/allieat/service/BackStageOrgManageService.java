package com.allieat.service;

import com.allieat.dto.UpdateOrgDTO;

import java.util.Map;

public interface BackStageOrgManageService {
    Map<String, Object> getInitData();
    Map<String, Object> getUpdateInitData(Integer id);
    Map<String, Object> updateOrgData(UpdateOrgDTO updateData);
    Map<String, Object> newOrgData(UpdateOrgDTO inputData);
}
