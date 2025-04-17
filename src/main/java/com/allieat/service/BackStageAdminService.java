package com.allieat.service;


import com.allieat.dto.ChangePasswordDTO;
import com.allieat.dto.NewAdminDTO;
import org.springframework.http.ResponseEntity;

public interface BackStageAdminService {
    NewAdminDTO newAdmin(NewAdminDTO newAdminDTO);
    ChangePasswordDTO changePassword(ChangePasswordDTO changePasswordDTO);
}
