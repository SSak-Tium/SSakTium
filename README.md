# < Spring User Setting V0.1 >

> 프로젝트 시작 시 반복되는 회원가입, 로그인 기능 등을 쉽게 세팅하기 위해 구현한 Repository

## ⚽ Functions
- 유저 회원가입
- 유저 로그인
- 유저 권한 변경
- 유저 조회
- 유저 비밀번호 변경

## ⚽ How to Use ?

![image](https://github.com/user-attachments/assets/e13064f8-1896-42ba-8a8e-e0fc35073e93)

application.yml 에서 spring.application.name 수정 후 $DB_URL 설정 후 사용 가능

- Environment variables
    - ${DB_URL}           // DB URL
    - ${DB_USERNAME}      //DB 유저 이름
    - ${DB_PASSWORD}      // DB 비밀번호
    - ${JWT_SECRET_KEY}   // JWT SECRET KEY


## ⚽ Settings
- Package Structure

  ![image](https://github.com/user-attachments/assets/ae42afd3-cc62-480c-b0a7-107bf7e5559c)

- Spring Secrity, JWT Token 적용
    - JWT Token 방식 [userId, email, userRole 암호화]

  ![image](https://github.com/user-attachments/assets/96b56b5b-f009-4a55-9f5c-09774fd5ca55)

- ApiResponse
    - 응답 형식 통일화

  ![image](https://github.com/user-attachments/assets/1b4ec4be-f84e-4334-a6d4-cd92a0b1b39e)





## ⚽ API

#### ▶ AuthController

| METHOD | Function | URL | Request Header | Request | Response |
|--------|--------|--------|--------|--------|--------|
| POST  | 회원가입  | /auth/signup  |   | 요청 body:<br>{<br>&nbsp;&nbsp;“email” : “이메일”,<br>&nbsp;&nbsp;“password” : “비밀번호”,<br>&nbsp;&nbsp;“userRole”: “사용자 권한”<br>}  | {<br>&nbsp;&nbsp;"status": "상태",<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"bearerToken": "Bearer <JWT Token>"<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;"message": "요청 처리 여부 메세지"<br>}  |
| POST  | 로그인  | /auth/signin  |   | 요청 body:<br>{<br>&nbsp;&nbsp;“email” : “이메일”,<br>&nbsp;&nbsp;“password” : “비밀번호”,<br>}  | {<br>&nbsp;&nbsp;"status": "상태",<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"bearerToken": "Bearer <JWT Token>"<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;"message": "요청 처리 여부 메세지"<br>}  |


#### ▶ UserAdminController

| METHOD | Function | URL | Request Header | Request | Response |
|--------|--------|--------|--------|--------|--------|
| PATCH  | 유저 권한 변경  | /admin/users/{userId}  | Authorization: Bearer JWT Token  | 요청 path<br>요청 body:<br>{<br>&nbsp;&nbsp;“role”: “사용자 권한”<br>}  | {<br>&nbsp;&nbsp;"status": "상태",<br>&nbsp;&nbsp;"data": "유저 권한이 정상적으로 변경되었습니다.",<br>&nbsp;&nbsp;"message": "요청 처리 여부 메세지"<br>}  |


#### ▶ UserController

| METHOD | Function | URL | Request Header | Request | Response |
|--------|--------|--------|--------|--------|--------|
| GET  | 유저 단건 조회 (id)  | /users/{userId}  | Authorization: Bearer JWT Token  | 요청 path | {<br>&nbsp;&nbsp;"status": "상태",<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"id": "유저 ID",<br>&nbsp;&nbsp;&nbsp;&nbsp;"email": "유저 EMAIL"<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;"message": "요청 처리 여부 메세지"<br>}  |
| POST  | 유저 비밀번호 변경  | /users  | Authorization: Bearer JWT Token  | 요청 path<br>요청 body:<br>{<br>&nbsp;&nbsp;“oldPassword” : “이전 비밀번호”,<br>&nbsp;&nbsp;“newPassword” : “새 비밀번호”,<br>} | {<br>&nbsp;&nbsp;"status": "상태",<br>&nbsp;&nbsp;"data": {<br>&nbsp;&nbsp;&nbsp;&nbsp;"id": "유저 ID",<br>&nbsp;&nbsp;&nbsp;&nbsp;"email": "유저 EMAIL"<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;"message": "요청 처리 여부 메세지"<br>}  |

---

### ⚽ Update
> 2024.10.12 v1.0 
# spring-user-setting-repo
