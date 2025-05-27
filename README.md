# allieat-backstage-lite

這是一份從 allieat 專案中擷取並重構的部分後台系統，專注於管理端的核心功能，內容包含：

- JWT 登入系統  
  使用 JWT 實作登入與權限驗證機制，並透過 Redis 管理 Token 黑名單。

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
- Redis（作為 JWT 黑名單快取）
- RESTful API 設計
- Maven 套件管理

## 專案結構如下

- 控制器 / 服務 / DTO 三層清晰分離
- 後台模組（登入、首頁、單位、捐款）具備獨立路由與封裝邏輯
- JWT 驗證攔截器與 WebConfig 可彈性配置


## 資料庫初始化（SQL 匯入）

請先啟動本機 MySQL 環境，並執行以下指令來初始化資料庫：

```bash
mysql -u root -p < sql/init_allieat.sql
```

- 預設資料庫名稱：`allieatfinal_db01`,預設資料庫密碼：`123456`
- 預設管理員帳號密碼：帳號: a001 / 密碼 :pwa001
- SQL 內容包含：
  - 資料表建立（organization, administrator, donationrecord...）
  - 初始測試資料匯入

SQL 檔案路徑：`/sql/init_allieat.sql`

此資料庫檔案與原始專案共用。

---

## Redis 啟動與設定說明

本專案使用 Redis 作為 JWT 黑名單機制的快取工具，請依照下列方式啟動 Redis 服務：

### 📦 安裝方式（擇一）

#### macOS / Linux 使用 Homebrew 安裝：
```bash
brew install redis
redis-server
```

#### Windows 使用建議：
- 安裝 [Memurai](https://www.memurai.com/) 或 [Redis for Windows](https://github.com/microsoftarchive/redis/releases)
- 啟動 `redis-server.exe`

#### 使用 Docker 快速啟動：
```bash
docker run --name redis-allieat -p 6379:6379 -d redis
```

### 🔧 專案預設 Redis 設定：

- 主機：`localhost`
- 埠號：`6379`
- 密碼：無（如需設密碼，請修改 `application.properties`）

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

你可以使用 `redis-cli` 驗證連線狀況：

```bash
redis-cli
ping  # 回傳 PONG 表示 Redis 運作正常
```

---
