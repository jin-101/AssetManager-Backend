# Asset Manager _ [자산관리] 하이브리드 앱
개발 기간 : 6주 (23.05.29 ~ 23.07.03)
<br/>
개발 인원 : 4명

## 목차
- [1️⃣프로젝트 개요](#1️⃣프로젝트-개요)
- [2️⃣개발 환경](#2️⃣개발환경)
- [3️⃣시스템 개요](#3️⃣시스템-개요)
- [4️⃣테이블 구조](#4️⃣테이블-구조)
- [5️⃣시스템 구조](#5️⃣시스템-구조)
- [6️⃣프로그램 화면](#6️⃣프로그램-화면)
- [7️⃣중요 소스코드](#7️⃣중요-소스코드)

## 1️⃣프로젝트 개요


## 2️⃣개발환경
<table>
  <tbody>
    <tr>
      <th>Language</th>
      <td>
        <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
        <img src="https://img.shields.io/badge/sql-4479A1?style=for-the-badge">
        <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
      </td>
    </tr>
    <tr>
      <th>Tool</th>
      <td>
        <img src="https://img.shields.io/badge/eclipse-2C2255?style=for-the-badge&logo=eclipseide&logoColor=white">
        <img src="https://img.shields.io/badge/React-61DAFB.svg?&style=for-the-badge&logo=React&logoColor=white">
        <img src="https://img.shields.io/badge/Visual%20Studio%20Code-007ACC.svg?&style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white">
        <img src="https://img.shields.io/badge/Androidstudio-3DDC84.svg?&style=for-the-badge&logo=Androidstudio&logoColor=white">
      </td>
    </tr>
    <tr>
      <th>DataBase</th>
      <td>
        <img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
        <img src="https://img.shields.io/badge/mongodb-47A248?style=for-the-badge&logo=mongodb&logoColor=white">
      </td>
    </tr>
    <tr>
      <th>Server</th>
      <td>
        <img src="https://img.shields.io/badge/SpringBoot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white">
      </td>
    </tr>
     <tr>
      <th>Library</th>
      <td>
        <img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=Selenium&logoColor=white">
        <img src="https://img.shields.io/badge/JPA-E8E8E8?style=for-the-badge">
        <img src="https://img.shields.io/badge/POI3.7ver-E8E8E8?style=for-the-badge">
      </td>
    </tr>
  </tbody>
</table>

## 3️⃣시스템 개요
### Asset Manager란
7가지 자산군(예적금, 자동차, 부동산, 주식, 코인, 외환, 금)에 대한 종합적인 자산관리 및 여러가지 통계 기능(개인 재무비율지표)을 제공해주는 하이브리드 앱입니다. 또한, 가계부 입력을 통해 연말정산 예상환급액 계산도 가능합니다.

<!--
### 시스템 기능
|분류|기능|비고|
|:------|:------|:------|
|로그인|회원 로그인|비활성화된 고객사는 로그인 X, <br/> 고객사 로그인 시 기업관리, 입고관리 메뉴는 보이지 않음.|
|자산 추가|고객사 기업 등록/조회/수정/비활성화 기능|활성화,비활성화 여부에 따른 필터링 기능 구현, <br/> 삭제 기능 대신 비활성화 기능으로 안정성 고려.|
|상품 관리|고객사 상품 등록/조회/수정/비활성화 기능|상품 활성화, 비활성화 여부에 따른 필터링 기능 구현, <br/> 삭제 기능 대신 비활성화 기능으로 안정성 고려.|
|발주 관리|자동 발주 상품 목록 조회, <br/> 발주 상품 등록/상세조회 기능| 안전재고 > 현재재고 상태의 상품들만 자동 발주 상품 목록에 뜨도록 구현. <br/> 발주코드는 B-기업코드-시퀀스(날짜)로 생성 하여 Unique성 고려.|
|입고 관리|발주서 연동 자동 입고 등록 기능, <br/> 상품별 수동 입고 등록 기능, <br/> 수동입고를 위한 상품 통합 검색 기능, <br/> 입고 품목 수정,삭제,확정 기능, <br/> 확정 입고 내역 조회 기능|발주서 연동 시 재확인을 위한 상세 조회 기능 추가. <br/> 입고 코드는 I-기업코드-시퀀스(날짜)로 생성하여 Unique성 고려. <br/> 각 처리 시 확정여부를 다시한번 묻는 모달창 생성.
|출고 관리|주문건 수동 등록, 자동 등록 기능, <br/> 주문건 상세 조회 기능|주문건 자동 등록은 엑셀 파일 업로드시 자동 등록 되도록 구현, <br/> 출고 코드는 O-기업코드-시퀀스(날짜)로 생성하여 Unique성 고려.|
|입출고 내역 조회|입/출고 내역 조회 기능|입고/출고 별 필터링 기능 구현.|
|통계서비스| 기간별, 카테고리별, 상품별 매출 통계 기능 | 표와 차트 동시 제공. <br/> 고객기업사가 물류 이외의 다른 업무에 집중할 수 있도록 매출 통계 부가서비스를 제공하여 다른 3PL 플랫폼과의 차별성을 둠. |
-->

## 4️⃣테이블 구조
<img src="https://github.com/jin-101/AssetManager-Frontend/assets/126077503/ce843394-46a9-4d69-bdd0-26fad2d7640b" width="70%" />

## 5️⃣시스템 구조
### 1) 시스템 기능도 1
<img src="https://github.com/jin-101/AssetManager-Frontend/assets/126077503/4c947625-0e0a-4e7c-bca9-f434a4ba3cad" width="70%" />

### 2) 시스템 기능도 2
<img src="https://github.com/jin-101/AssetManager-Frontend/assets/126077503/71f1a7de-6eea-473c-8dae-46268644d59f" width="70%" />

## 6️⃣프로그램 화면
<a href="https://www.youtube.com/watch?v=hM4pjhtgEkc&t=17s">시연영상 1</a>
<br/>
<a href="https://www.youtube.com/watch?v=b4TEy7flwmk&t=7s">시연영상 2</a>

## 7️⃣중요 소스코드 (각 팀원별)
### 1) 추가 중
<!--
<img src="https://github.com/ITEM-FARM/itemFarmProject/assets/49816869/4e1b01d0-d505-459a-a368-d15321639f50" width="70%"><br/>
-->
(부가 설명)

### 2) 

### 3) 

### 4) 

### 5) 

### 6) 

### 7) 

### 8) 

### 9) 

### 10) 

### 11) 

### 12) 

### 13) 

### 14) 

### 15) 

### 16) 

### 17) 

### 18) 

