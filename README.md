# ☕️ Americanote ☕️ &nbsp;&nbsp; ![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/dajeongdev/Americanote/deploy.yml?branch=develop)
![중간산출물_Americanote](https://github.com/dajeongdev/Americanote/assets/61612976/6956afbf-def5-4fc8-af36-4e9689304868)

(➕ 비사이드 온라인 해커톤 포텐데이 403에 참가하여 19팀 중 무려 **2등**🥈을 달성했습니다! 팀원분들께 정말 감사합니다!)

## 📃 서비스 소개 📃
"카페 리뷰는 많은데 정작 커피 맛은 어떤지 모르겠어💦" <br>
"꼭 방문해서 먹어보고 커피 맛을 알아야 할까?" <br>
"내 취향에 딱 맞는 아메리카노를 먹고싶어!" <br>
그래서 만들었습니다! Americanote는 카페를 방문하지 않고도 커피의 향, 강도, 산미를 알 수 있어요!
<br/>
<br/>
🖥️&nbsp;&nbsp;[서비스 이용해보러 가기](https://americanote.vercel.app) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/dajeongdev/Americanote/assets/61612976/89d0ce20-287b-450a-a54a-2cfec5efb661" width="16px">&nbsp;&nbsp;
[노션 구경하러 가기](https://night-geography-507.notion.site/Americanote-c95e62599ec348c48893a668d5dcfb5c?pvs=4)
<br/>
<br/>
<br/>


## 😽 핵심 기능 😽
- 나의 커피 취향을 저장할 수 있어요.
- 카페를 가기 전, 커피의 맛을 확인할 수 있어요.
- 지도에서 원하는 가격, 향, 강도, 산미 등을 필터링하여 볼 수 있어요.
- 나의 커피 취향에 맞는 카페들을 추천받을 수 있어요.
<br/>
<br/>
 

## 🛠 Tech Stack 🛠 
<table border="3">
  <th align="center">Role</th>
  <tr>
    <td rowspan="6" align="center"><b>Backend</td>
    <td><img src="https://staging.svgrepo.com/show/184143/java.svg" width="16px" alt="_icon" />&nbsp;&nbsp;<b>Java</td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/112257466/209075280-78be8487-7d6a-485c-92a8-d6677f0caab9.png" width="15px" alt="_icon" />&nbsp;&nbsp;<b>Spring Boot</td>
    <tr>
	<td><img src="https://user-images.githubusercontent.com/112257466/209075280-78be8487-7d6a-485c-92a8-d6677f0caab9.png" width="15px" alt="_icon" />&nbsp;&nbsp;<b>Spring Security</td>
    <tr>
	<td><img src="https://user-images.githubusercontent.com/112257466/209078356-d9120e3d-9498-4ee4-a38d-139a263910f4.png" width="14px" alt="_icon" />&nbsp;&nbsp;<b>MySQL</td>
	<tr>
	<td><img src="https://user-images.githubusercontent.com/112257466/209076523-777fe02a-455f-48a0-a4b1-aeb9fff17b10.png" width="16px" alt="_icon" />&nbsp;&nbsp;<b>JPA/Data JPA</td>
	<tr>
    <td><img src="https://github.com/GDSC-Team-J/ADDI-ML/assets/112257466/dff863c4-fb90-4747-a621-bdbd2c44a0be" width="16px" alt="_icon" />&nbsp;&nbsp;<b>QueryDSL</td>
  </tr>
  <tr>
    <td rowspan="11" align="center"><b>Infra</td>
    <td><img src="https://yt3.googleusercontent.com/ytc/AIf8zZTAG01_SUWCNq2jcOvl49us-MaQ0THgkfJwRnIO=s900-c-k-c0x00ffffff-no-rj" width="15px" alt="_icon" />&nbsp;&nbsp;<b>Naver Cloud Server</td>
  </tr>
  <tr>
    <td><img src="https://yt3.googleusercontent.com/ytc/AIf8zZTAG01_SUWCNq2jcOvl49us-MaQ0THgkfJwRnIO=s900-c-k-c0x00ffffff-no-rj" width="15px" alt="_icon" />&nbsp;&nbsp;<b>Naver Cloud MySQL</td>
  </tr>
  <tr>
    <td><img src="https://yt3.googleusercontent.com/ytc/AIf8zZTAG01_SUWCNq2jcOvl49us-MaQ0THgkfJwRnIO=s900-c-k-c0x00ffffff-no-rj" width="15px" alt="_icon" />&nbsp;&nbsp;<b>Naver Cloud Global Domain</td>
  </tr>
  </tr>
</table>
<br/>
<br/>


## 🌲 서비스 아키텍처 🌲
<img width="918" alt="service-architecture" src="https://github.com/dajeongdev/Americanote/assets/61612976/26eccda0-7573-4a6f-8736-049b69d9bc41">
<br/>
<br/>
<br/>


## 🌱 ERD 🌱
<img width="1134" alt="스크린샷 2024-04-02 22 51 19" src="https://github.com/dajeongdev/Americanote/assets/61612976/13644c7f-da8a-4707-b7cc-ff5ea12b701c">
<br/>
<br/>
<br/>


## 📙 API Docs 📙
[Swagger 문서 확인하러 가기](https://www.americanote.store/swagger-ui/index.html)
<br/>
<br/>
<br/>


## ❗️ 리팩토링
- [x]  리팩토링 항목 정리 (0401)
- [x] [서버 구성 정리](https://night-geography-507.notion.site/e81154ce879a4ca5a80a925ef33e67e8?pvs=4)
- [x]  validation & error 처리 -> 파트 나눠서 해보고 코드 리뷰 완료
- [x]  CI/CD
	- [x] Github Actions self-hosted runner를 사용한 배포 자동화 (0406, 0422)
- [x]  토큰 서비스 개선 @박다정 (0410)
- [x]  쿼리 최적화 -> N+1 문제 해결 (0403)
- [x]  크롤링 코드 개선 @최다빈
- [x]  accessToken -> userId 컴포넌트화 @박다정
- [x]  클래스 접근제어자 수정 @박다정
- [x]  readme 추가 (0402)
<br/>
<br/>


## 폴더 구조
```markdown
├── AmericanoApplication.java
├── cafe
│   ├── config
│   │   └── CafeSwaggerConfig.java
│   ├── controller
│   │   └── CafeController.java
│   ├── domain
│   │   ├── CafeWithHasLike.java
│   │   ├── entity
│   │   │   ├── Cafe.java
│   │   │   └── RecentSearch.java
│   │   ├── request
│   │   │   └── SearchCafeRequest.java
│   │   └── response
│   │       ├── CafeDetailResponse.java
│   │       ├── CafePreviewResponse.java
│   │       ├── CafeResponse.java
│   │       └── CafeSearchResponse.java
│   ├── repository
│   │   ├── CafeRepository.java
│   │   ├── RecentSearchRepository.java
│   │   └── querydsl
│   │       ├── CafeQueryRepository.java
│   │       └── CafeQueryRepositoryImpl.java
│   └── service
│       ├── AddressToCoordinate.java
│       ├── CafeService.java
│       └── CrawlingCafe.java
├── coffee
│   ├── controller
│   │   └── CoffeeController.java
│   ├── domain
│   │   ├── entity
│   │   │   ├── Coffee.java
│   │   │   └── CoffeeFlavour.java
│   │   └── response
│   │       ├── CoffeeFlavourDegreeResponse.java
│   │       └── CoffeeResponse.java
│   ├── repository
│   │   ├── CoffeeFlavourRepository.java
│   │   ├── CoffeeRepository.java
│   │   └── querydsl
│   └── service
│       └── CoffeeService.java
├── common
│   ├── contributor
│   │   └── CustomFunctionContributor.java
│   ├── entity
│   │   ├── BaseEntity.java
│   │   ├── Degree.java
│   │   ├── ErrorCode.java
│   │   ├── Flavour.java
│   │   └── UserRole.java
│   ├── exception
│   │   ├── CommonException.java
│   │   ├── CommonExceptionHandler.java
│   │   ├── CommonValidationException.java
│   │   ├── CustomException.java
│   │   ├── TokenException.java
│   │   └── UserException.java
│   ├── response
│   │   ├── BasicApiSwaggerResponse.java
│   │   ├── CommonResponse.java
│   │   └── ErrorResponse.java
│   └── validator
│       └── CommonValidator.java
├── config
│   ├── QueryDslConfig.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── folder.txt
├── like
│   ├── config
│   │   └── LikeSwaggerConfig.java
│   ├── controller
│   │   └── LikeController.java
│   ├── domain
│   │   ├── Like.java
│   │   └── UserCafePK.java
│   ├── repository
│   │   └── LikeRepository.java
│   └── service
│       └── LikeService.java
├── mypage
│   ├── config
│   │   └── MypageSwaggerConfig.java
│   ├── controller
│   │   └── MypageController.java
│   └── service
│       └── MyPageService.java
├── review
│   ├── controller
│   │   └── ReviewController.java
│   ├── domain
│   │   ├── ReviewRequest.java
│   │   ├── entity
│   │   │   └── Review.java
│   │   └── response
│   │       └── ReviewResponse.java
│   ├── repository
│   │   └── ReviewRepository.java
│   └── service
│       └── ReviewService.java
├── security
│   ├── handler
│   │   ├── CustomAccessDeniedHandler.java
│   │   └── CustomAuthenticationEntryPoint.java
│   ├── jwt
│   │   ├── filter
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtFailureFilter.java
│   │   └── util
│   │       └── JwtTokenProvider.java
│   └── service
│       └── CustomUserDetailService.java
└── user
    ├── config
    │   └── UserSwaggerConfig.java
    ├── controller
    │   └── UserController.java
    ├── domain
    │   ├── entity
    │   │   ├── User.java
    │   │   ├── UserFlavour.java
    │   │   └── UserToken.java
    │   ├── request
    │   │   ├── KakaoLoginRequest.java
    │   │   ├── UserPreferRequest.java
    │   │   └── UserRequest.java
    │   └── response
    │       └── UserResponse.java
    ├── repository
    │   ├── UserFlavourRepository.java
    │   ├── UserRepository.java
    │   └── UserTokenRepository.java
    └── service
        ├── KakaoLoginService.java
        ├── UserService.java
        └── UserTokenService.java
```
<br/>
<br/>
