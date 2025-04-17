package com.allieat.controller;


import com.allieat.dto.ChangePasswordDTO;
import com.allieat.dto.NewAdminDTO;
import com.allieat.service.BackStageAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/backStage")
public class BackStageAdminController {
    @Autowired
    private BackStageAdminService backStageNewAdminService;

    @PostMapping("/admin")
    public ResponseEntity<NewAdminDTO> newAdmin(@RequestBody @Valid NewAdminDTO newAdmin) {
        try {
            return ResponseEntity.ok(backStageNewAdminService.newAdmin(newAdmin));
        } catch (Exception e) {
            NewAdminDTO respDTO = new NewAdminDTO();
            respDTO.setStatus("fail");
            return ResponseEntity.internalServerError().body(respDTO);
        }
    }

    @PutMapping("/admin/changePassword")
    public ResponseEntity<ChangePasswordDTO> changePassword(@RequestBody @Valid ChangePasswordDTO Admin) {
        try {
            return ResponseEntity.ok(backStageNewAdminService.changePassword(Admin));
        } catch (Exception e) {
            ChangePasswordDTO respDTO = new ChangePasswordDTO();
            respDTO.setStatus("fail");
            return ResponseEntity.internalServerError().body(respDTO);
        }
    }

    // 以下為驗證錯誤處理，僅限本控制器使用，處理項目為MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "fail");
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        String message = "驗證失敗";

        if (!errors.isEmpty()) {
            FieldError firstError = errors.get(0);
            message = firstError.getDefaultMessage();
        }
        result.put("message", message);
        return ResponseEntity.badRequest().body(result);
    }
}
