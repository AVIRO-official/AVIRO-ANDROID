# AVIRO
<img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource126/v4/ad/78/8a/ad788a0c-00b4-3859-fca1-310cf17bfb43/0d867ebd-4237-4046-b0d5-61dbb3d2d0de_1290_2796-1.png/1290x2796bb.png" width="280" height="560"> <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource116/v4/a0/61/45/a061455a-880c-8d21-3c83-9a624a1263b0/ed9198bf-8d10-42b2-ad9b-261381a6abfe_1290_2796-2.png/1290x2796bb.png" width="280" height="560">

## 소개
- 비건 식당, 카페, 술집 등 음식점에 비건 음식이있거나, 모든 음식이 비건인 식당을 구분해서 공유하는 플랫폼입니다.
- 식당을 직접 추가하거나 삭제할 수 있습니다.
- 메뉴의 요청사항을 따로 기입할 수 있습니다.
- 식당의 후기를 작성해서 유저들끼리 정보를 공유할 수 있습니다.


## 🗺️ Information

### Features
0. 소셜 로그인 (애플) / 회원가입
1. 비건 식당 맵
2. 식당 정보 상세보기
3. 가게 등록하기
4. 후기 작성하기
5. 마이페이지 / 챌린지
6. 식당 정보 수정하기
7. 비건 식당 검색하기
8. 스플래시

### Technology Stack
* Tools : Android Studio Chipmunk
* Language : Kotlin
* Architecture Pattern : 안드로이드 권장 아키텍쳐 + MVVM 
* Android Architecture Components(AAC)

### Library
* OKHTTP3
* RETROFIT2
* Apple Sign In
* Naver Map
* Realem
* Hilt

## Foldering
```
.
├── data 
│   ├── datasource
│   │    ├── AuthDataSouDataSourceImp
│   │    ├── AuthDataSouDataSource
│   │    ├── MemberDataSouDataSourceImp
│   │    └── MemberDataSouDataSource
│   │
│   ├── repository (구현부)
│   │     ├── MemberRepositoryImp
│   │     └── AuthRepositoryImp
│   ├── api
│   │     ├── MemberService
│   │     └── AuthService 
│   ├── di
│   │     ├── ApiModule
│   │     ├── RepositoryModule
│   │     └── DataSourceModule 
│   └── entity
│         ├── auth
│         │    ├── AuthRequestDTO
│         │    └── AuthResponseDTO
│         ├── member
│         │    ├── NicknameCheckRequest
│         │    └── NicknameCheckResponse
│         └── base
│              ├── BaseResponse
│              └── DataBodyResponse
│
├── domain  (여러 레포지토리 사용할 수 있음)
│   ├── repository (interface)
│   │     ├── MemberRepository
│   │     └── AuthRepository
│   │
│   └── usecase 
│         ├── auth 
│         │    ├── Login 
│         │    └── Logout
│         ├── member 
│         │    ├── CreateMemberUseCase 
│         │    ├── GetMyInfoUseCase
│         │    ├── LogoutUseCase
│         │    ├── UpdateMyNicnameUseCase
│         │    └── WithdrawUseCas
│         ├── retaurant 
│         │    ├── CreateRetaurantUseCase
│         │    ├── UdateRetaurantUseCase 
│         │    ├── GetRetaurantUseCase 
│         │    ├── DeleteRetaurantUseCase
│         │    ├── SearchRetaurantUseCase
│         │    ├── BookmarkRetaurantUseCase
│         │    ├── ReportRetaurantUseCase //신고하기
│         │    └── ShareRetaurantUseCase
│         ├── review
│         │    ├── CreateReviewUseCase
│         │    ├── UdateReviewUseCase
│         │    ├── GetReviewUseCase
│         │    ├── ReportReviewUseCase //신고하기
│         │    └── DeleteReviewUseCase
│         └── menu 
│              ├── CreateMenuUseCase //카테고리 정보도 포함
│              ├── UdateMenuUseCase
│              ├── GetMenuUseCase
│              └── DeleteMenuUseCase
│
│
├── presentation (여러 유스케이스 사용할 수 있음)
│   ├── ui 
│   │   ├── splash
│   │   │      ├── Splash
│   │   │      └── SplashViewModel
│   │   ├── gide
│   │   │      ├── Guide 
│   │   │      ├── GuideMenuFragment
│   │   │      ├── GuidePagerAdapter
│   │   │      ├── GuideRegisterFragment
│   │   │      ├── GuideReviewFragment
│   │   │      ├── GuideSearchFragment
│   │   │      └── GuideViewModel
│   │   ├── sign  
│   │   │      ├── Sign 
│   │   │      ├── SignSocialFragment
│   │   │      ├── SignNicknameFragment
│   │   │      ├── SignOptionFragment
│   │   │      ├── SignTermsFragment
│   │   │      ├── SignCompeletFragment
│   │   │      └── SignViewModel
│   │   │
│   │   ├── home
│   │   │      ├── map 
│   │   │      ├── mypage
│   │   │      ├── register
│   │   │      └── Home
│   │   │       
│   │   ├── update
│   │   │      ├── Update 
│   │   │      ├── UpdateHomepageFragment
│   │   │      ├── UpdateLocFragment
│   │   │      ├── UpdateNumberFragment
│   │   │      ├── UpdateTimetableFragment
│   │   │      └── SignViewModel



```
