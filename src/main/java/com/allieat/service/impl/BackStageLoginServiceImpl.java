package com.allieat.service.impl;


import com.allieat.dto.AdminDTO;
import com.allieat.repository.jdbc.BackStageLoginDao;

import com.allieat.util.JwtUtil;
import com.allieat.entity.AdminVO;
import com.allieat.service.BackStageLoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackStageLoginServiceImpl implements BackStageLoginService {
    @Autowired
    private BackStageLoginDao backStageLogin;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Map<String, Object> findByAccount(AdminDTO admin) {
        // 資料Mapping
        AdminVO adminVO = new AdminVO();
        // 資料mapping
        BeanUtils.copyProperties(admin, adminVO);

        List<AdminVO> adminList = backStageLogin.findByAccount(adminVO);

        if (adminList.isEmpty()) {
            return createResult("account does not exist");
        }
        AdminVO foundAdmin = adminList.get(0); // 取出結果

        if (!StringUtils.hasText(foundAdmin.getPassword())
                || ! encoder.matches(admin.getPassword(), foundAdmin.getPassword())) {
            return createResult("login failed password is incorrect"); // 密碼錯誤
        }

        // 產生Token
        String token = jwtUtil.generateToken(foundAdmin.getAccount());
        // 回傳值設定
        Map<String, Object> result = createResult("login ok");
        result.put("token", token);
        return result; // 密碼正確

    }

    // 用於回傳狀態創建
    private Map<String, Object> createResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("loginState", message);
        return result;
    }
}