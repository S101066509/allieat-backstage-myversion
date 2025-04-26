package com.allieat.service.impl;

import com.allieat.dto.UpdateOrgDTO;
import com.allieat.repository.OrganizationRepository;
import com.allieat.entity.OrganizationVO;
import com.allieat.service.BackStageOrgManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.allieat.constant.OrgManageConstant.*;

@Service
public class BackStageOrgManageServiceImpl implements BackStageOrgManageService {
    @Autowired
    private OrganizationRepository data;

    @Override
    public Map<String, Object> getInitData() {
        Map<String, Object> result = new HashMap<>();
        List<OrganizationVO> orgInitDataList = data.findAll();
        result.put("orgInitDataList", orgInitDataList);
        return result;
    }

    public Map<String, Object> getUpdateInitData(Integer id) {
        Map<String, Object> result = new HashMap<>();
        if (id == null) {
            result.put("success", false);
            result.put("message", ORG_ID_MISSING);
            return result;
        }

        Optional<OrganizationVO> selectedData = data.findById(id);
        if (selectedData.isEmpty()) {
            result.put("success", false);
            result.put("message", ORG_NOT_FOUND);
            return result;
        }
        result = convertOrgToMap(selectedData.get());
        return result;
    }
    @Transactional
    public Map<String, Object> updateOrgData(UpdateOrgDTO updateData) {
        Map<String, Object> result = new HashMap<>();
        Integer id = updateData.getOrganizationId();
        // ID驗證
        if (id == null) {
            result.put("success", false);
            result.put("message", ORG_ID_MISSING);
            return result;
        }

        // 確認資料是否存在
        Optional<OrganizationVO> selectedData = data.findById(id);
        if (selectedData.isEmpty()) {
            result.put("success", false);
            result.put("message",  ORG_NOT_FOUND);
            return result;
        }
        // 查詢資料
        OrganizationVO savedData = selectedData.get();
        // DTO 資料mapping到VO，但排除id欄位避免變成新增。
        BeanUtils.copyProperties(updateData, savedData, "organizationId", "createdTime");
        // 儲存資料，需要是被查詢到的那筆作更新。
        data.save(savedData);
        // 製作回傳格式
        result = convertOrgToMap(savedData);
        result.put("success", true);
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> newOrgData(UpdateOrgDTO inputData) {
        Map<String, Object> result = new HashMap<>();
        OrganizationVO newData = new OrganizationVO();
        // DTO 資料mapping到VO，忽略id因為是自增。
        BeanUtils.copyProperties(inputData, newData, "organizationId", "createdTime");
        // 重名檢測
        Optional<OrganizationVO> existing = data.findByName(inputData.getName());
        if (existing.isPresent()) {
            result.put("success", false);
            result.put("error", ORG_DUPLICATE );
            return result;
        }
        newData.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        newData = data.save(newData);
        // 製作回傳格式
        result = convertOrgToMap(newData);
        result.put("success", true);
        return result;
    }

    private Map<String, Object> convertOrgToMap(OrganizationVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("organizationId", vo.getOrganizationId());
        map.put("name", vo.getName());
        map.put("type", vo.getType());
        map.put("status", vo.getStatus());
        map.put("createdTime", vo.getCreatedTime());
        return map;
    }

}




