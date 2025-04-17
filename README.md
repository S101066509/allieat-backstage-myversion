
# allieat-backstage-lite

這是一份從 allieat 專案中擷取並重構的部分後台系統，專注於管理端的核心功能，內容包含：

- JWT 登入系統  
  使用 JWT 實作安全的登入與權限驗證流程。

- 首頁即時數據（長輪詢）  
  利用 `DeferredResult` 與排程機制實作長輪詢，首頁可即時更新捐款金額與人數等資訊。

- 單位管理模組（Org）  
  包含單位的查詢、審核、新增與修改等完整後台功能。

- 捐款紀錄管理模組（Donated）  
  提供捐款資料的新增、編輯、查詢等後台 API，並處理自然人／法人身份類別的資料格式。

本專案為團隊專案的一部分，原始完整專案請參考：  
https://github.com/Flyting0314/allieat

此版本僅保留我本人負責開發與重構的後台功能模組，並進行清晰的模組劃分與程式優化。

---

## 技術架構

- Java 17
- Spring Boot
- Spring MVC
- JWT（JSON Web Token）
- RESTful API 設計
- Maven 套件管理

## 專案結構如下
- 控制器 / 服務 / DTO 三層清晰分離
- 後台模組（登入、首頁、單位、捐款）具備獨立路由與封裝邏輯
- JWT 驗證攔截器與 WebConfig 可彈性配置
