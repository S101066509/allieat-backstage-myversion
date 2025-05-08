package com.allieat.service.impl;


import com.allieat.constant.LoginConstant;
import com.allieat.dto.AdminDTO;
import com.allieat.repository.jdbc.BackStageLoginDao;

import com.allieat.util.JwtUtil;
import com.allieat.entity.AdminVO;
import com.allieat.service.BackStageLoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BackStageLoginServiceImpl implements BackStageLoginService {
    @Autowired
    private BackStageLoginDao backStageLogin;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private StringRedisTemplate redis;


    @Override
    public Map<String, Object> findByAccount(AdminDTO admin) {
        // 資料Mapping
        AdminVO adminVO = new AdminVO();
        // 資料mapping
        BeanUtils.copyProperties(admin, adminVO);

        List<AdminVO> adminList = backStageLogin.findByAccount(adminVO);

        if (adminList.isEmpty()) {
            return createResult(LoginConstant.ACCOUNT_NOT_EXIST);
        }
        AdminVO foundAdmin = adminList.get(0); // 取出結果

        if (!StringUtils.hasText(foundAdmin.getPassword())
                || ! encoder.matches(admin.getPassword(), foundAdmin.getPassword())) {
            return createResult(LoginConstant.PASSWORD_INCORRECT); // 密碼錯誤
        }

        // 產生Token
        String token = jwtUtil.generateToken(foundAdmin.getAccount());
        System.err.println(token);
        // 回傳值設定
        Map<String, Object> result = createResult(LoginConstant.SUCCESS);
        result.put("token", token);
        return result; // 密碼正確

    }

    public Map<String, Object> logout(String token) {
        // 解析 Token，並獲取創建時間
        Date creationTime = jwtUtil. getCreationTime(token);
        // 計算創建時間與當前時間的差異，來計算 TTL
        long currentTimeMillis = System.currentTimeMillis();
        long creationTimeMillis = creationTime.getTime();
        long ttlMillis = currentTimeMillis - creationTimeMillis;  // 已存在的時間差
        long tokenMaxLifetimeMillis = 30 * 60 * 1000;  // 30 分鐘（假設 token 的有效期是 30 分鐘）
        long remainingTtlMillis = tokenMaxLifetimeMillis - ttlMillis;

        //計算剩餘有效時間並設置 TTL（以秒為單位）
        long ttlSeconds = remainingTtlMillis / 1000;

        if (ttlSeconds > 0) {
            // 將 token 加入黑名單並設定過期時間
            redis.opsForValue().set("blacklist:" + token, "true", remainingTtlMillis, TimeUnit.MILLISECONDS);// 設定過期時間
            return createResult("Logout successful");
        } else {
            // token 已過期，直接返回錯誤。
            return createResult("Token has already expired");
        }
    }



    // 用於回傳狀態創建
    private Map<String, Object> createResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("loginState", message);
        return result;
    }
}