## CLD4 Archive – Material Manager

클라우드 엔지니어링 교육 과정에서 강의/자료를 관리하는 Spring Boot 애플리케이션입니다.
초심자도 읽고 따라할 수 있도록 JPA, Lombok, Spring Security, Thymeleaf, 부트스트랩 사용법을 함께 설명합니다.

### 주요 기능

- 사용자 관리(관리자/강사/훈련생) + 승인 흐름(강사/훈련생은 승인 후 로그인 가능)
- 강의 등록/목록, 자료 등록/목록, 즐겨찾기
- 검색/정렬/필터, 페이지네이션(자료 목록)
- 부트스트랩 기반 UI, 브랜드 컬러(primary=#ff6000)

### 기술 스택

- Java 17, Spring Boot 3.5.x
- Spring Web, Spring Data JPA, Thymeleaf
- Lombok(보일러플레이트 코드 제거)
- H2(개발용), MySQL(운영용)
- Gradle 빌드

### 프로젝트 구조(요약)

```
src/main/java/com/example/materialmanager
  ├─ config/            # 보안/웹 설정
  ├─ controller/        # MVC 컨트롤러
  ├─ domain/            # JPA 엔티티(테이블 매핑)
  ├─ dto/               # 화면/전송용 DTO
  ├─ repository/        # Spring Data JPA 리포지토리
  └─ service/           # 비즈니스 로직

src/main/resources/templates
  ├─ _fragments/        # 공통 레이아웃/헤더/푸터
  ├─ auth/              # 로그인/회원가입
  ├─ users/             # 사용자 관리 화면
  ├─ lecture/           # 강의 화면
  └─ material/          # 자료 화면
```

### 빠른 시작

1. 자바/그래들 준비: Java 17, Gradle(래퍼 동봉)

2. 개발용 H2로 실행

```
./gradlew bootRun
```

기본 포트: http://localhost:8080

3. 테스트 실행

```
./gradlew test
```

4. 빌드

```
./gradlew clean build
```

### 데이터베이스 설정

- 기본은 H2(메모리/파일) 개발 프로파일을 사용합니다.
- MySQL 사용 시 `application.yml` 또는 OS 환경변수로 접속 정보를 지정하세요.

예시(환경변수):

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/materialdb
SPRING_DATASOURCE_USERNAME=material
SPRING_DATASOURCE_PASSWORD=secret
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### JPA 빠른 설명

- 엔티티: `@Entity` 로 테이블과 매핑. 예) `User`, `Lecture`, `Material`, `Favorite` 등
- 기본키: `@Id @GeneratedValue`
- 열 매핑: `@Column`, 열거형: `@Enumerated(EnumType.STRING)`
- 생성 시각: `@PrePersist` 훅에서 기본값 세팅
- 리포지토리: `JpaRepository<Entity, ID>` 상속만으로 CRUD 제공 + 메서드 명으로 쿼리 파생(예: `findByEmail`, `findByApproved`)

예시 – 간단한 엔티티

```java
@Entity
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role; // ADMIN, TEACHER, STUDENT
  @Column(nullable = false)
  private boolean approved = false; // 승인 여부
  private LocalDateTime approvedAt;
  private LocalDateTime createdAt;
  @PrePersist void onCreate(){ if(createdAt==null) createdAt=LocalDateTime.now(); }
}
```

### Lombok 빠른 설명

- 보일러플레이트(게터/세터/생성자/빌더)를 자동 생성합니다.
- 주요 어노테이션:
  - `@Getter @Setter` 필드 접근자
  - `@NoArgsConstructor @AllArgsConstructor @Builder` 생성자/빌더
- IDE에서 Lombok 플러그인이 필요할 수 있습니다.

### 승인 흐름 개요

1. 회원가입 시 ADMIN 외 사용자는 `approved=false`
2. 관리자가 사용자 목록/상세에서 승인 버튼 클릭 → `approved=true`, `approvedAt` 세팅
3. 승인 전에는 로그인 시 차단 메시지 노출

### Thymeleaf & 부트스트랩

- 레이아웃 조각: `/_fragments/layout.html`의 `head/header/footer/scripts` 사용
- 브랜드 컬러: primary = `#ff6000` (버튼/링크 등 기본 톤 통일)
- 배지/테이블/알림 스타일은 전역 `<style>`에 유틸 클래스로 정의

### 페이지네이션 표시 규칙(자료 목록)

- 자료가 없을 때는 페이지네이션을 렌더링하지 않습니다.
  - 템플릿 조건: `th:if="${page != null and page.totalElements > 0}"`

### 코드 컨벤션(요약)

- 서비스: 비즈니스 규칙/검증, 컨트롤러는 입출력과 라우팅 최소화
- DTO 사용으로 화면/도메인 분리 권장
- 의미 있는 메서드명, 널/에러 핸들링, 조기 반환(guard clause)

### 자주 겪는 이슈

- Lombok 미작동: IDE 플러그인 설치, `annotationProcessor 'org.projectlombok:lombok'` 확인
- H2 콘솔/프레임 관련 보안 헤더: 개발 단계에서만 예외 처리

### 학습 자료 추천

- Spring 공식 문서: `https://docs.spring.io/spring-boot/docs/current/reference/html/`
- Spring Data JPA: `https://docs.spring.io/spring-data/jpa/docs/current/reference/html/`
- Thymeleaf 가이드: `https://www.thymeleaf.org/documentation.html`
- Lombok: `https://projectlombok.org/features/all`
- Bootstrap 5: `https://getbootstrap.com/docs/5.3/getting-started/introduction/`

### 라이선스

교육/학습 목적의 예제 프로젝트로 자유롭게 참고/수정할 수 있습니다.
