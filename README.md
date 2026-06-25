# LifePolicy Platform — 後端 API 服務

Spring Boot 壽險平台後端，提供保單申請、理賠管理、約訪排程、用戶認證等功能。

- **運行 Port**：`8085`
- **資料庫**：MySQL（`ins_crm`）
- **API 文件**：啟動後開啟 `http://localhost:8085/swagger-ui.html`

---

## 技術棧

| 層次 | 技術 |
|------|------|
| 框架 | Spring Boot 3.x |
| 安全 | Spring Security 6 + JJWT (Stateless JWT) |
| ORM | MyBatis-Plus（`policyapplication` 模組）+ MyBatis XML（其餘模組）|
| 資料庫 | MySQL 8 |
| 驗證 | Jakarta Validation (`@Valid` + `BindingResult`) |
| 文件 | SpringDoc OpenAPI 3 (Swagger UI) |
| 工具 | Lombok、MapStruct |

---

## 模組架構

```
maventest/
├── Application.java                    # 啟動入口
├── auth/                               # 用戶認證與帳號管理
├── claim/                              # 理賠申請與審核
├── dashboard/                          # 首頁統計數據
├── customer/                           # 客戶資料管理
├── appointment/                        # 約訪排程管理
├── policy/                             # 保單審核歷程
├── common/                             # 共用元件（例外、安全、回應格式）
└── policyapplication/                  # 保單申請 DDD 模組
    ├── domain/                         # 領域實體、列舉
    ├── application/internal/           # 應用服務（command / query）
    ├── infrastructure/repository/      # MyBatis mapper、Repository 實作
    └── interfaces/rest/                # REST 控制器
```

---


### 上傳目錄

```yaml
app:
  upload-dir: D:/claim-uploads/   # 理賠附件存放位置，需確保服務帳號有寫入權限
```

---

## API 端點總覽

所有端點（登入、Swagger UI 除外）皆需在 `Authorization` header 帶入 Bearer Token。

### 認證 `/api/v1/auth`

| Method | 路徑 | 描述 | 所需角色 |
|--------|------|------|----------|
| POST | `/login` | 登入，回傳 JWT | 公開 |
| GET | `/me` | 取得目前登入者資訊 | 任意已登入 |
| POST | `/register` | 新增 APPLICANT 帳號 | REVIEWER, ADMIN |
| GET | `/users` | 取得所有使用者列表 | REVIEWER, ADMIN |
| PUT | `/user/{username}` | 修改帳號（含角色、狀態） | ADMIN |
| DELETE | `/user/{username}` | 刪除帳號 | ADMIN |

### 理賠管理 `/api/admin/claim`

| Method | 路徑 | 描述 |
|--------|------|------|
| GET | `/list` | 查詢理賠清單（可篩選狀態、保單號、申請日） |
| GET | `/{claimNo}` | 查看理賠詳情 |
| POST | `/` | 新增理賠紀錄（狀態設為 SUBMIT） |
| PUT | `/{claimNo}` | 更新理賠資料 |
| DELETE | `/{claimNo}` | 刪除案件（僅 PENDING 狀態可刪）|
| POST | `/upload` | 上傳理賠附件 |
| GET | `/member-options` | 客戶下拉清單 |
| GET | `/policy-options` | 保單下拉清單 |
| GET | `/agent-options` | 經辦人員下拉清單 |

### 理賠審核 `/api/admin/claim-audit`

| Method | 路徑 | 描述 |
|--------|------|------|
| GET | `/list` | 待審核理賠清單 |
| PUT | `/decision` | 審核核決（PENDING / APPROVED / REJECTED / RETURN） |
| GET | `/logs/{claimNo}` | 查詢審核歷程 |

### Dashboard `/api/dashboard`

| Method | 路徑 | 描述 |
|--------|------|------|
| GET | `/summary` | 首頁統計（業績、保單狀態分布、SLA、月目標） |

### 客戶管理 `/api/customer`

| Method | 路徑 | 描述 |
|--------|------|------|
| GET | `/` | 查詢客戶列表 |
| POST | `/` | 新增客戶 |
| PUT | `/{id}` | 更新客戶資料 |

### 約訪排程 `/api/appointment`

| Method | 路徑 | 描述 |
|--------|------|------|
| POST | `/` | 建立約訪（驗證保單名單狀態） |
| PUT | `/confirm` | 確認約訪結果（成功 / 失敗） |
| PUT | `/batch` | 批次更新約訪記錄 |

### 保單歷程 `/api/policy-log`

| Method | 路徑 | 描述 |
|--------|------|------|
| GET | `/{policyNo}` | 查詢保單審核歷程 |

### 保單申請（DDD 模組）

| Method | 路徑 | 描述 |
|--------|------|------|
| POST | `/api/v1/insurance/applications` | 建立保單申請 |
| GET | `/api/v1/insurance/applications` | 查詢申請列表 |
| PUT | `/api/v1/insurance/applications/{id}/review` | 審核（核准 / 退件） |
| PUT | `/api/v1/insurance/applications/{id}` | 更新申請（僅 RETURN 狀態） |
| GET/PUT/DELETE | `/api/v1/products/**` | 商品管理 |
| GET | `/api/notifications/**` | 通知查詢與標記已讀 |

---

## 安全機制

### JWT 驗證流程

```
Request
  └─ JwtAuthenticationFilter
       ├─ tryAuthenticateWithAuthJwt(token)   ← 新版 JwtTokenProvider（auth.jwt.secret）
       │     成功 → SecurityContext = AppUserPrincipal
       │     失敗 → 繼續
       └─ tryAuthenticateWithLegacyJwt(token) ← 舊版 JwtUtil（app.jwt.secret）
             成功 → SecurityContext = String (username)
             失敗 → 無認證，依端點授權規則處理
```

### 角色體系

| 角色碼 | 說明 | 授予方式 |
|--------|------|----------|
| `APPLICANT` | 保戶 / 申請人 | 由 ADMIN 透過 `/register` 建立 |
| `REVIEWER` | 業務 / 核保員 | 由 ADMIN 透過 `/user/{username}` 更新 |
| `ADMIN` | 系統管理員 | 同上 |

Spring Security 比對規則：`hasRole("ADMIN")` → 對應 JWT claim `roleCode: "ADMIN"` → Authority `ROLE_ADMIN`。

---

## 錯誤處理

### 例外類別層次

```
RuntimeException
└── ApiException(code, message, HttpStatus)      ← 所有業務例外的基底
      ├── BusinessRuleException                  ← 違反業務規則，回 422 Unprocessable Entity
      └── ErrorInputException                    ← 輸入格式錯誤，回 400 Bad Request
```

拋出範例：

```java
// 資源不存在
throw new ApiException(
    ApiCode.APPLICATION_NOT_FOUND.getCode(),
    ApiCode.APPLICATION_NOT_FOUND.getMessage(),
    HttpStatus.NOT_FOUND
);

// 業務規則違反（自動 422）
throw new BusinessRuleException(
    ApiCode.REVIEW_STATUS_INVALID.getCode(),
    ApiCode.REVIEW_STATUS_INVALID.getMessage()
);

// 輸入驗證失敗（自動 400）
throw new ErrorInputException(
    ApiCode.INPUT_INVALID.getCode(),
    "policyNo is required"
);
```

### GlobalExceptionHandler 攔截規則

```
ApiException         → HTTP status 由 exception.getHttpStatus() 決定
                       回應體：ApiResponse<null>{ status, message }

MethodArgumentNotValidException
                     → 400 Bad Request
                       回應體：第一條驗證錯誤訊息

Exception（兜底）    → 500 Internal Server Error
                       回應體：e.getMessage()
```

### 統一回應格式

本專案目前並行使用兩種回應格式，依模組不同：

**`ReturnMsg<T>`**（auth、appointment、policy-log、dashboard 模組）

```json
{
  "CODE": "INS-0000",
  "MESSAGE": "SUCCESS",
  "SUCCESS": true,
  "DATA": { ... }
}
```

**`ApiResponse<T>`**（claim、customer、policyapplication 模組）

```json
{
  "status": 200,
  "message": "SUCCESS",
  "data": { ... }
}
```

### ApiCode 錯誤碼清單（部分）

| 錯誤碼 | 說明 |
|--------|------|
| `INS-0000` | 成功 |
| `INS-COM-4000` | 輸入參數驗證失敗 |
| `INS-AUTH-4010` | 未認證 |
| `INS-AUTH-4011` | 帳號或密碼錯誤 |
| `INS-AUTH-4012` | Token 無效或過期 |
| `INS-AUTH-4030` | 無操作權限 |
| `INS-AUTH-4031` | 帳號已停用 |
| `INS-POL-4002` | 商品不存在 |
| `INS-POL-4003` | 申請案件不存在 |
| `INS-POL-4227` | 相同被保人與商品已有進行中申請 |
| `INS-POL-4228` | 審核狀態流程不合法 |
| `INS-SYS-5000` | 系統錯誤 |

---

## 交易管理（Transaction Rollback）

### 標注 `@Transactional` 的操作

所有寫入操作皆使用 `@Transactional(rollbackFor = Exception.class)`，代表任何 `Exception`（包含 checked exception）都會觸發完整 rollback。

| 類別 | 方法 | 涵蓋操作 |
|------|------|----------|
| `ClaimAuditController` | `makeAuditDecision()` | UPDATE tb_claim + INSERT tb_claim_aprv_log + 推播通知 |
| `CallAppointmentCommandServiceImpl` | `createAppointment()` | INSERT tb_call_appointment + UPDATE tb_call_list |
| `CallAppointmentCommandServiceImpl` | `confirmAppointmentResult()` | UPDATE tb_call_appointment + UPDATE tb_call_list |
| `AptRecordSingleServiceImpl` | 批次更新 | 多筆 apt_record 更新 |
| `InsuranceApplicationRepositoryImpl` | 各 save / update | 保單申請相關寫入 |

### Rollback 觸發情境說明

#### 1. 理賠審核核決（ClaimAuditController.makeAuditDecision）

```
BEGIN TRANSACTION
  ① updateClaim(status, approveAmount, remark, updateUser)
  ② insertAprvLog(claimNo, policyNo, status, ...)
  ③ pushClaimNotification(...)        ← 推播失敗不 rollback（通知服務內部吞例外）
COMMIT
```

**風險**：若 `claim.getAgentId()` 為 `null`，`appUserRepository.findById(null)` 在 mapper 層可能拋出 `IllegalArgumentException`。因為 `pushClaimNotification()` 在 transaction 邊界之內，此例外會導致 ①② 一起 rollback，審核紀錄遺失，但 HTTP 回應已是 200 OK 的情況下 client 端並不知情。

**建議**：在 `pushClaimNotification()` 開頭對 `claim.getAgentId()` 進行 null 檢查，或將通知推播移至 transaction 提交後執行（`@TransactionalEventListener(phase = AFTER_COMMIT)`）。

#### 2. 建立約訪（CallAppointmentCommandServiceImpl.createAppointment）

```
BEGIN TRANSACTION
  ① insertCallAppointment(entity)
  ② updateListStatus(listNo, APPOINTED)
COMMIT
```

若 ② 失敗（例如 listNo 不存在），① 的約訪記錄一起 rollback，保持資料一致性。

#### 3. 確認約訪結果（confirmAppointmentResult）

```
BEGIN TRANSACTION
  ① confirmAppointmentResult(sno, recallResult, recTime, updateUser)
  ② updateListStatus(listNo, CLOSED)
COMMIT
```

若 ① 回傳 `updated == 0`（資料不存在或已確認），拋出 `IllegalArgumentException` 使整個交易 rollback。

### 通知推播不屬於交易範圍

`NotificationService.push()` 內部使用 try-catch 吞掉所有例外並記錄 error log，確保推播失敗**不影響業務交易**的提交結果。

```java
// NotificationService.java
private void push(...) {
    try {
        notificationMapper.insert(entity);
    } catch (Exception e) {
        log.error("推播通知失敗 recipient={} title={}", recipientUsername, title, e);
        // 不 rethrow — 通知失敗不阻斷業務流程
    }
}
```

---



## 開發指南

### 新增端點

1. 在對應模組的 `controller/` 建立或修改 Controller
2. 確認 `SecurityConfig.java` 中已為該路徑配置適當的授權規則
3. 業務邏輯例外一律拋出 `ApiException`（或其子類），不要在 Controller 層 catch 後吞掉

### 新增交易操作

```java
@Transactional(rollbackFor = Exception.class)
public void yourWriteOperation(...) {
    // 1. 執行 DB 寫入
    mapper.insert(entity);
    // 2. 若需推播通知，在 COMMIT 後透過 @TransactionalEventListener 執行
    //    避免推播例外觸發不預期的業務資料 rollback
}
```

### 錯誤回應標準做法

```java
// 正確：拋出 ApiException，由 GlobalExceptionHandler 統一處理
throw new ApiException(ApiCode.APPLICATION_NOT_FOUND.getCode(),
        ApiCode.APPLICATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);

// 錯誤：回傳 e.getMessage() 給 client（洩露內部資訊）
return ResponseEntity.status(500).body("Error: " + e.getMessage());
```
