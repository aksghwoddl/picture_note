

# <img width="10%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/211609129-00d9dc7d-7f21-47f6-bb54-722f111e707b.png">
# 📷 원하는 그림을 찾아보자!! '픽쳐노트'


## 🤔 프로젝트 설명
> 여러개의 그림을 감상하고 좋아하는 그림을 즐겨찾기에 등록 할 수 있습니다.

<br>

### 💻 기술스택 
#### ▪️ Client
<p>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
<img src="https://img.shields.io/badge/Room-003B57?style=for-the-badge&logo=SQLite&logoColor=white">
<img src="https://img.shields.io/badge/RxBinding-B7178C?style=for-the-badge&logo=ReactiveX&logoColor=white">
<img src="https://img.shields.io/badge/Retrofit-3E4348?style=for-the-badge&logo=Square&logoColor=white">
<img src="https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=Square&logoColor=white">
<img src="https://img.shields.io/badge/DataBinding-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/MVVM-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Coroutine-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Hilt-0F9D58?style=for-the-badge&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Glide-0F9D58?style=for-the-badge&logo=&logoColor=white">
</p>

#### ▪️ Server
<p>
<img src="https://img.shields.io/badge/OpenAPI-3776AB?style=for-the-badge&logo=&logoColor=white">
</p>
<br>

### 🛠 구현 사항
##### 1️⃣ 그림 보여주기
###### Rest 통신을 통해 서버에 저장된 그림과 정보를 가져오는 기능 리스트가 마지막에 도달하면 자동으로 추가 이미지를 불러옵니다.
<div align="center">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/211614697-6c20628e-9223-441b-a391-888920aaa98c.gif">
</div>

##### 2️⃣ 그림 상세 페이지
###### 선택한 그림의 상세 페이지 화면으로 다음 / 이전 버튼을 통해 이미지를 손쉽게 감상 할 수 있습니다.

<div align="center">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/211615397-3244fa36-2ccb-4502-9021-3e5f17ff5aab.gif">
 </div>
 
##### 3️⃣ 즐겨찾기 등록/해제 및 즐겨찾기 페이지
###### RoomDB를 통해 사용자가 좋아하는 그림을 즐겨찾기에 저장 할 수 있습니다 등록한 그림은 즐겨찾기 페이지에서 확인이 가능합니다. <br>
###### 즐겨찾기 페이지는 RoomDB에 저장된 이미지 목록을 보여주는 화면으로 Drag and Drop을 통해 이미지의 순서를 조정할 수 있고 즐겨찾기를 해제 할 수 있습니다.
<p align="center">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/211616386-8a804b52-9ac8-4013-80bd-97f1700bcb4e.gif">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/211617547-e1461bc0-2560-4bea-90e3-cf150e366a6b.gif">
</p>

##### 4️⃣ 스크롤 최상단 이동 버튼
###### Floating Button을 통해 RecyclerView의 최상단으로 이동할 수 있도록 구현 하였습니다. <br>
<div align="center">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/212238434-80be93da-6d15-4e11-b36d-a0365b546cce.gif">
</div>

##### 5️⃣ 그림 검색 페이지
###### 사용자가 설정한 넓이 , 높이와 GrayScale , Blur설정에 따른 랜덤한 이미지를 검색하여 보여주는 기능을 구현 하였습니다.
<div align="center">
 <img width="30%" src="https://user-images.githubusercontent.com/65700842/216884499-bbf42fc6-514e-4c97-bacb-eaa555a26f47.gif">
</div>

### 😎 프로젝트 사용기술 설명
##### 1️⃣ Dagger Hilt를 활용하여 의존성을 주입 해주었습니다.
##### 2️⃣ MVVM 디자인 기반으로 프로젝트를 진행 하였습니다.
##### 3️⃣ Coroutine을 통한 비동기 처리를 , RxBinding을 통한 UI event 처리를 하였습니다. (throttleFirst()를 통한 이중 클릭 방지)
##### 4️⃣ Retrofit2를 통해 Rest통신을 하였습니다.
##### 5️⃣ Room을 활용하여 내부 저장소에 즐겨찾기 정보를 저장하도록 구현 하였습니다
##### 6️⃣ Glide를 통해 이미지 작업을 진행 하였습니다.
##### 7️⃣ Repository를 사용하여 Data를 관리 하였습니다.
##### 8️⃣ ItemTouchHelper를 사용하여 Drag and Drop 기능을 구현 하였습니다.
##### 9️⃣ ListAdapter , DiffUtil , AsyncListDiffer를 사용하여 RecyclerView의 Adapter를 구현 하였습니다.
##### 🔟 Clean Architecture를 위해 Module화를 통해 각 Layer를 분리해주었습니다.

