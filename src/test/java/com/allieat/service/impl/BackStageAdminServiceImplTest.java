package com.allieat.service.impl;

import com.allieat.dto.ChangePasswordDTO;
import com.allieat.dto.NewAdminDTO;
import com.allieat.entity.AdminVO;
import com.allieat.repository.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class BackStageAdminServiceImplTest {
    //Mock=>假的Repossitory
    @Mock
    private AdminRepository adminRepository;
    //Mock=>假的Encoder
    @Mock
    private PasswordEncoder encoder;

    //加入實際服務
    @InjectMocks
    private BackStageAdminServiceImpl service;

    /*----- 新增帳號的測試 -----*/

    @Test
    @DisplayName("創建帳號成功")
    void newAdminSuccess(){
        //創建假的Dto
        NewAdminDTO dto = new NewAdminDTO();
        dto.setAccount("admin");
        dto.setPassword("123");
        //模擬資料庫內沒有這個資料，表示可以新增
        Mockito.when(adminRepository.findByAccount("admin")).thenReturn(Optional.empty());
        //模擬加密後的密碼
        Mockito.when(encoder.encode("123")).thenReturn("hashedPassword");
        //回傳的DTO
        NewAdminDTO result = service.newAdmin(dto);

        //判斷回傳的狀態為成功
        assertEquals("success", result.getStatus());
        //驗證是否真的有跑adminRepository.save()，若有則假裝存入新創的帳號與密碼
        Mockito.verify(adminRepository).save(Mockito.argThat(admin ->
                admin.getAccount().equals("admin") &&
                        admin.getPassword().equals("hashedPassword") &&
                        admin.getCreatedTime() != null
        ));
    }
    @Test
    @DisplayName("創建帳號失敗")
    void newAdminFailure(){
        NewAdminDTO dto = new NewAdminDTO();
        dto.setAccount("admin");
        dto.setPassword("123");
        //模擬有找到帳號
        Mockito.when(adminRepository.findByAccount("admin")).thenReturn(Optional.of(new AdminVO()));

        NewAdminDTO result = service.newAdmin(dto);
        //判斷回傳的狀態為帳號已存在
        assertEquals("帳號已存在", result.getStatus());
        //驗證save這個方法沒有被呼叫到
        Mockito.verify(adminRepository, Mockito.never()).save(Mockito.any());
    }


    /*-----更改帳號的測試 -----*/
    @Test
    @DisplayName("更改密碼成功")
    void changePasswordSuccess(){
        //創建假的Dto，傳進來的資料
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setAccount("admin");
        dto.setOldPassword("123");
        dto.setNewPassword("456");
        // 模擬資料庫中的舊密碼
        AdminVO admin = new AdminVO();
        admin.setAccount("admin");
        admin.setPassword("123");

        //模擬找到這個帳號，並取得舊資料
        Mockito.when(adminRepository.findByAccount("admin")).thenReturn(Optional.of(admin));
        //比對舊密碼正確，回傳正確
        Mockito.when(encoder.matches("123","123")).thenReturn(true);
        //模擬存入新密碼
        Mockito.when(encoder.encode("456")).thenReturn("HashedPassword");
        //回傳的DTO
        ChangePasswordDTO result = service.changePassword(dto);

        //判斷回傳的狀態為成功
        assertEquals("success", result.getStatus());


        //驗證是否真的有跑adminRepository.save()，若有則存入新的密碼。
        Mockito.verify(adminRepository).save(Mockito.argThat(s ->
                s.getAccount().equals("admin") &&
                        s.getPassword().equals("HashedPassword")
        ));
    }
    @Test
    @DisplayName("舊密碼錯誤")
    void changePasswordFailure(){
        //創建假的Dto，傳進來的資料
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setAccount("admin");
        dto.setOldPassword("234");
        dto.setNewPassword("456");
        // 模擬資料庫中的舊密碼
        AdminVO admin = new AdminVO();
        admin.setAccount("admin");
        admin.setPassword("123");

        //模擬找到這個帳號，並取得舊資料
        Mockito.when(adminRepository.findByAccount("admin")).thenReturn(Optional.of(admin));
        //比對舊密碼錯誤，回傳false
        Mockito.when(encoder.matches("234","123")).thenReturn(false);

        //回傳的DTO
        ChangePasswordDTO result = service.changePassword(dto);

        //判斷回傳的狀態為成功
        assertEquals("舊密碼錯誤", result.getStatus());

        //驗證save這個方法沒有被呼叫到
        Mockito.verify(adminRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    @DisplayName("沒找到帳號")
    void changePasswordNotFound(){
        //創建假的Dto，傳進來的資料
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setAccount("CanNotFound");
        dto.setOldPassword("234");
        dto.setNewPassword("456");

        //模擬找到這個帳號，並取得舊資料
        Mockito.when(adminRepository.findByAccount("CanNotFound")).thenReturn(Optional.empty());

        //回傳的DTO
        ChangePasswordDTO result = service.changePassword(dto);

        //判斷回傳的狀態為成功
        assertEquals("帳號不存在", result.getStatus());

        //驗證save這個方法沒有被呼叫到
        Mockito.verify(adminRepository, Mockito.never()).save(Mockito.any());
    }

}