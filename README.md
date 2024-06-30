# KakaoCafe ORDER API
## 개발 환경
- 기본환경
    - IDE : Intellij IDEA Ultimate
    - OS : Mac OS X
    - Language : Kotlin Java17
    - GIT
- Framework & Library
    - Spring Boot 3.3.1
    - JPA
    - H2
    - Redis (Embedded)
    - Junit
---
## API spec
## 회원가입  
### HTTP request
/v1/members/join
```  
POST /v1/members/join  
Content-Type: application/json;charset=UTF-8    
Host: localhost:8080  

{
  "email": "example@naver.com",
  "password": "password123",
  "name": "JohnD oe",
  "gender": "MALE",
  "phoneNumber": "123-456-7890",
  "birthDay": "1990-01-01"
}  
```
### Example response
```
HTTP/1.1 200 OK
```
## 로그인  
### HTTP request
/v1/members/login
```
POST /v1/members/login
Content-Type: application/json;charset=UTF-8
Host: localhost:8080

{
  "email": "example@naver.com",
  "password": "password123"
}
```
### Example response
```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJleGFtcGxlQG5hdmVyLmNvbSIsIm1lbWJlclN0YXR1cyI6Ik9LIiwic3ViIjoidXNlciIsImV4cCI6MTcxOTg0MjM0NX0.MhM4uHHFWtoSjMe2FwscCmWuouH-Z9CFnrBzwLTs83M",
    "memberStatus": "OK"
}
```
## 회원탈퇴  
### HTTP request
/v1/members/quit
```
DELETE /v1/members/quit
Content-Type: application/json;charset=UTF-8
x-kakaocafe-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJleGFtcGxlQG5hdmVyLmNvbSIsIm1lbWJlclN0YXR1cyI6Ik9LIiwic3ViIjoidXNlciIsImV4cCI6MTcxOTg0MjM0NX0.MhM4uHHFWtoSjMe2FwscCmWuouH-Z9CFnrBzwLTs83M
Host: localhost:8080
```
### Example response
```
HTTP/1.1 200 OK
```

## 주문
### HTTP request
```
POST /v1/members/quit
Content-Type: application/json;charset=UTF-8
x-kakaocafe-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJleGFtcGxlQG5hdmVyLmNvbSIsIm1lbWJlclN0YXR1cyI6Ik9LIiwic3ViIjoidXNlciIsImV4cCI6MTcxOTg0MjM0NX0.MhM4uHHFWtoSjMe2FwscCmWuouH-Z9CFnrBzwLTs83M
Host: localhost:8080

{
  "orderProducts": [
    {
      "productId": 1,
      "quantity": 1
    },
    {
      "productId": 2,
      "quantity": 1
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```
### Example response
```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "orderId": 1
}
```
## 주문 취소
### HTTP request
```
DELETE /v1/orders/{orderId}
Content-Type: application/json;charset=UTF-8
x-kakaocafe-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJleGFtcGxlQG5hdmVyLmNvbSIsIm1lbWJlclN0YXR1cyI6Ik9LIiwic3ViIjoidXNlciIsImV4cCI6MTcxOTg0MjM0NX0.MhM4uHHFWtoSjMe2FwscCmWuouH-Z9CFnrBzwLTs83M
```
### Example response
```
HTTP/1.1 200 OK
```
## 결제
### HTTP request
```
POST /v1/payments
Content-Type: application/json;charset=UTF-8
x-kakaocafe-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJleGFtcGxlQG5hdmVyLmNvbSIsIm1lbWJlclN0YXR1cyI6Ik9LIiwic3ViIjoidXNlciIsImV4cCI6MTcxOTg0MjM0NX0.MhM4uHHFWtoSjMe2FwscCmWuouH-Z9CFnrBzwLTs83M

{
    "orderId":1,
    "amount":17000.00
}
```
### Example response
```
HTTP/1.1 200 OK
```
