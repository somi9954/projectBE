
# 오늘의 할일 : TODOLIST
**오늘의 할일을 의미있게 계획하고자 적는 리스트**

## 1. 개요
- 프로젝트명 : 오늘의 할일 : TODOLIST
- 개발 기간 : 2023.12.01~
- 개발 인원 : 1명

|조소미|
|:---:|
|[somi9954](https://github.com/somi9954)|
![](https://avatars.githubusercontent.com/u/137499604?v=4)|

## 2. ⚙️기술 스택
### ✔️프론트엔드
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black"><img src="https://img.shields.io/badge/Css-1572B6?style=for-the-badge&logo=Css&logoColor=white"><img src="https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white"><img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"><img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
### ✔️Back-end
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=green"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=yellow"><img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtoken&logoColor=white">


# 📋 기능 명세서
## 메인 페이지
![메인페이지](/images/main.png)

## 1. 관리자 페이지
### 기본 설정
- 사이트 설정
![사이트설정](/images/사이트%20설정.png)

### 회원관리
- 회원 전체 조회
  ![사이트설정](/images/회원관리.png)
- 아이디 찾기
- 비밀번호 찾기

## 2. 회원
### 로그인
- 로그인
### 회원가입
- 회원가입 시 암호화(hashing)화 되어 DB에 저장.
- Id(email) : email 형식의 아이디. 필수 항목.
- Pw : 최대 길이 40. 필수 항목.
- 회원명 : 최대 길이 40. 필수 항목.

### 아이디(email) 찾기
- 아이디(email), 회원명으로 조회
- 성공시 정보 출력 후 로그인 페이지로 이동.
- 실패시 재입력 요구.

### 비밀번호 찾기
- 아이디(email), 회원명으로 조회
- 성공시 정보 출력 후 로그인 페이지로 이동.
- 실패시 재입력 요구.

### 마이페이지
- 개인정보 수정, 회원탈퇴
![마이페이지](/images/mypage.png)
![개인정보수정](/images/savemember.png)
![회원탈퇴](/images/회원탈퇴.png)

## 3.TODOLIST
- TODOLIST 목록
- TODOLIST 할일 추가
- TODOLIST 할일 삭제
![TODOLIST](/images/main.png)

