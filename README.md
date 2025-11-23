# allieat-backstage-lite

這是一份從 allieat 專案中擷取並重構的部分後台系統，專注於管理端的核心功能，內容包含：

- **JWT 登入系統**  
  使用 JWT 實作登入與權限驗證機制，並透過 Redis 管理 Token 黑名單。

- **首頁即時數據（長輪詢）**  
  利用 `DeferredResult` 與排程機制實作長輪詢，首頁可即時更新捐款金額與人數等資訊。

- **單位管理模組（Org）**  
  包含單位的查詢、審核、新增與修改等完整後台功能。

- **捐款紀錄管理模組（Donated）**  
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
- Redis（作為 JWT 黑名單快取）
- RESTful API 設計
- Maven 套件管理

---

## 專案結構

- Controller / Service / DTO 三層式架構
- 模組分離：Login / Dashboard / Organization / Donation
- JWT 驗證攔截器 + WebConfig 可配置化

---

# 使用 Docker 快速啟動專案

本專案已全面支援 Docker，一次建立即可啟動：

- Spring Boot 後端服務
- MySQL（自動匯入初始化 SQL）
- Redis（JWT 黑名單快取）

只需一個指令即可啟動整個環境。

---

## 1. 建立環境（Docker Compose）

請確認已安裝：

- Docker
- Docker Compose

接著在專案根目錄執行：

```bash
docker compose up --build
```

Docker 會自動：

1. 建立 MySQL（含 UTF8MB4 設定）
2. 自動匯入 `init-db/init_allieat.sql` 初始化資料
3. 啟動 Redis
4. 打包並啟動 Spring Boot

---

## 🗄 2. MySQL 資料庫資訊

- Host：`db`
- Port：`3306`
- 預設資料庫：`allieatfinal_db01`
- 帳號：`root`
- 密碼：`123456`

登入容器：

```bash
docker exec -it allieat-backstage-myversion-db-1 mysql -u root -p123456
```

---

## 3. Redis 啟動資訊

- Host：`redis`
- Port：`6379`
- 用途：JWT 黑名單快取

進入 Redis：

```bash
docker exec -it allieat-backstage-myversion-redis-1 redis-cli
```

---

## 4. API 伺服器位置

啟動後即可訪問：

```
http://localhost:8080
```
並使用
帳號：a001
密碼:pwa001
進入測試