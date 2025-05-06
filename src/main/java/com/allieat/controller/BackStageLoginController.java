package com.allieat.controller;



import com.allieat.constant. LoginConstant;
import com.allieat.dto.AdminDTO;
import com.allieat.service.BackStageLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/backStage")
public class BackStageLoginController {

    @Autowired
    private BackStageLoginService backStageLoginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AdminDTO admin) {
        Map<String, Object> result=backStageLoginService.findByAccount(admin);
        String check =(String) result.get("loginState");
        if(LoginConstant.ACCOUNT_NOT_EXIST.equals(check)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);//帳號不存在的回傳
        }
        if(LoginConstant.PASSWORD_INCORRECT.equals(check)) {
            return ResponseEntity.ok(result);//密碼錯誤的回傳
        }
        return ResponseEntity.ok(result);//正確回傳
    }

    //錯誤處理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleLocalException(Exception e) {
        System.err.println(e.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("loginState", LoginConstant.SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

