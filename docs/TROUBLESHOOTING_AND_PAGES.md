# 트러블슈팅 & 페이지 생성 정리

> Demo Docs 프로젝트 — 로그인/회원가입 UI 및 실행 이슈 대응 기록

---

## 1. 페이지 생성 (Material Design 3)

### 1.1 목표

- Google **Material Design 3** 기반 로그인·회원가입 화면
- 기존 REST API (`/api/auth/login`, `/api/auth/signup`)와 연동
- 로그인 성공 시 JWT를 브라우저 `localStorage`에 저장

### 1.2 추가된 파일

| 경로 | 설명 |
|------|------|
| `src/main/resources/static/login.html` | 로그인 페이지 |
| `src/main/resources/static/signup.html` | 회원가입 페이지 |
| `src/main/resources/static/home.html` | 로그인 후 홈 (프로필·로그아웃) |
| `src/main/resources/static/css/auth.css` | M3 색상·레이아웃 스타일 |
| `src/main/resources/static/js/auth-api.js` | API 호출·토큰 저장 공통 스크립트 |
| `src/main/java/.../controller/PageController.java` | URL → HTML 리다이렉트 |

### 1.3 사용 기술

- **Material Web** (`md-outlined-text-field`, `md-filled-button` 등) — CDN `esm.run/@material/web`
- **Roboto** + **Material Symbols** 아이콘
- M3 컬러 토큰 (Primary `#6750A4`, Surface Container 등)

### 1.4 접속 URL

| 페이지 | URL |
|--------|-----|
| 로그인 | http://localhost:8080/login.html 또는 `/login` |
| 회원가입 | http://localhost:8080/signup.html 또는 `/signup` |
| 홈 | http://localhost:8080/home.html |

`PageController`가 `/`, `/login`, `/signup`, `/home`을 각 HTML로 리다이렉트합니다.

### 1.5 프론트 ↔ API 연동

**로그인 (`login.html`)**

```http
POST /api/auth/login
Content-Type: application/json

{
  "user_login_id": "아이디",
  "user_password": "비밀번호"
}
```

- 성공 시 `access_token`, `refresh_token`을 `localStorage`에 저장
- `/home.html`로 이동

**회원가입 (`signup.html`)**

```http
POST /api/auth/signup
Content-Type: application/json

{
  "user_login_id": "아이디",
  "user_nickname": "닉네임",
  "user_email": "이메일",
  "user_password": "비밀번호"
}
```

- 성공 시 `/login.html?message=...` 로 이동 (로그인 안내)

**홈 (`home.html`)**

```http
GET /api/auth/me
Authorization: Bearer {access_token}
```

- 문서 API 테스트 버튼: `GET /api/docs/category/1` (JWT 필요)

### 1.6 WebMvcConfig와의 관계

정적 HTML/CSS/JS 서빙 및 Docs API JWT 검사는 `WebMvcConfig`에서 설정합니다.

- `addResourceHandlers` — `classpath:/static/` 등 정적 리소스
- `addInterceptors` — `/api/docs/**` 만 JWT 인터셉터 적용 (auth·HTML 경로는 제외)

---

## 2. 트러블슈팅

### 2.1 `npm run dev` 실패

**증상**

```
ENOENT: no such file or directory, open '.../package.json'
```

**원인**  
이 프로젝트는 **Spring Boot + Gradle**이지 Node/React 프로젝트가 아님.

**해결**

```powershell
cd D:\KDT_learning\spring\demo
.\gradlew bootRun
```

기본 포트: **8080**

---

### 2.2 Gradle JVM 8 오류

**증상**

```
Gradle requires JVM 17 or later. Your build is currently configured to use JVM 8.
```

**해결**  
Java 17+ (프로젝트는 Java 21 toolchain) 설정 후 실행.

```powershell
$env:JAVA_HOME = (Get-Command java | ForEach-Object { Split-Path (Split-Path $_.Source) })
.\gradlew bootRun
```

---

### 2.3 Spring Data — `No property 'user' found for type 'User'`

**증상**

```
Cannot create query for method [UserRepository.existsByUser_email(...)]
No property 'user' found for type 'User'
```

**원인**  
메서드명 `existsByUser_email`을 Spring Data가 `user` + `email` 속성으로 잘못 파싱. 실제 필드명은 `user_email`.

**해결**  
`UserRepository`, `RefreshTokenRepository`에 **`@Query` JPQL** 명시.

```java
@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.user_email = :email")
boolean existsByUser_email(@Param("email") String userEmail);
```

---

### 2.4 Spring Data — `No property 'category' found`

**증상**

```
Cannot create query for method [DocsRepository.findFirstByCategory_Category_idAndDoc_weightGreaterThan...]
No property 'category' found for type 'Category'; Traversed path: Docs.category
```

**원인**  
`Category_Category_id` 네이밍이 연관 객체 `category`의 하위 속성 `category`로 오해됨. 실제는 `category.category_id`.

**해결**  
`DocsRepository` 등 문제되는 Repository를 `@Query`로 변경.

```java
@Query("SELECT d FROM Docs d WHERE d.category.category_id = :categoryId AND d.doc_weight > :docWeight ORDER BY d.doc_weight ASC")
List<Docs> findNextByCategoryIdAndDocWeightGreaterThan(..., Pageable pageable);
```

동일 패턴으로 `HistoryRepository`, `LogRepository`, `ImageRepository`, `GrantRoleRepository`도 `@Query` 적용.

---

### 2.5 JDBC Dialect 오류 (SQLite)

**증상**

```
Cannot determine a dialect for JdbcTemplate; Please provide a Dialect
```

**원인**  
`spring-boot-starter-data-jdbc`와 JPA를 동시에 사용하면서 SQLite dialect 충돌.

**해결**  
JPA만 사용하므로 **`spring-boot-starter-data-jdbc` 의존성 제거**.

---

### 2.6 `No static resource login.html`

**증상**

```
NoResourceFoundException: No static resource login.html for request '/login.html'.
```

**원인**

1. 서버가 **정적 파일 추가 전**에 기동된 경우 (classpath에 `static/` 미반영)
2. Spring Boot 4에서 `webmvc`만 사용 시 정적 리소스 매핑이 불완전할 수 있음

**해결**

1. `WebMvcConfig`에 `addResourceHandlers`로 `classpath:/static/` 등록
2. `application.yml`에 정적 리소스 경로 설정

   ```yaml
   spring:
     web:
       resources:
         static-locations: classpath:/static/
         add-mappings: true
   ```

3. `spring-boot-starter-webmvc` → **`spring-boot-starter-web`** 로 변경
4. 반드시 **클린 재기동**

   ```powershell
   .\gradlew clean bootRun
   ```

**확인**  
빌드 후 `build/resources/main/static/login.html` 존재 여부 확인.

---

### 2.7 정적 파일 변경이 반영되지 않을 때

**조치**

- DevTools 사용 중이면 HTML/CSS 변경 후 **서버 재시작**이 더 확실함
- `.\gradlew clean bootRun` 권장

---

## 3. 빠른 체크리스트

| 확인 항목 | 기대 결과 |
|-----------|-----------|
| `src/main/resources/static/login.html` 존재 | O |
| `.\gradlew clean bootRun` 후 기동 | `Started DemoApplication` |
| http://localhost:8080/login.html | 로그인 화면 표시 |
| 회원가입 후 로그인 | `/home.html` 이동, `/api/auth/me` 성공 |
| Docs API | `Authorization: Bearer {token}` 헤더 필요 |

---

## 4. 참고 — 예전 XML 설정과의 대응

| 예전 (XML) | 지금 |
|------------|------|
| `spring-mvc.xml` — 정적 리소스, 인터셉터 | `WebMvcConfig.java` |
| `web.xml` — 서블릿 | Spring Boot 자동 설정 |
| MyBatis `*Mapper.xml` (SQL) | JPA `Repository` + `@Query` (이번 이슈와 무관) |

---

*작성 기준: 프로젝트 당일 작업 내역*
