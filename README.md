# Market-Gola
![로고](https://user-images.githubusercontent.com/71138398/197147455-3433a2d1-3379-48ff-a779-c8d5b0df87a8.jpg)

# 프로젝트 소개

신선식품을 판매하는 이커머스 서비스의 Rest API 서버를 만드는 프로젝트입니다. 

# 사용 기술

* Java 11
* Spring 5.3.20
* Spring Boot 2.7.0
* Mysql 8.0.29
* JPA 2.2.3
* Redis 4
* Github Actions
* AWS S3, Lambda

# 프로젝트 중점 사항

- 상황에 맞는 적절한 기술 선택
- 서버 확장을 고려한 설계
- 객체지향적이고 깔끔한 코드
- 꼼꼼하고 가독성 높은 테스트 코드
- Restful한 API

# 프로젝트 구조
![프로젝트 구조도 1](https://user-images.githubusercontent.com/71138398/197139995-fd5ff6e3-1f9c-44df-866b-cb9290e780da.png)

# 주요 기능

1. 유저 - 로그인, 회원 가입
2. 상품 - 단 건 조회, 여러 건 조회, 수정, 삭제
3. 주문

## 유저

#### UseCase

-   사용자는 '회원 가입', '로그인', '로그아웃'을 할 수 있다.

#### 고민

<details>
<summary>어떤 방식으로 로그인을 구현할까? JWT vs Session</summary>

### 보안 

JWT는 탈취될 경우 Access Token이 만료되기 전까지 해커가 마음대로 접근 가능하다.<br>
Session의 경우 Session을 만료시킴으로써 바로 접근 차단이 가능하다.<br>
따라서 Session이 보안상 더 뛰어나다.

### 서버에서 상태 저장

JWT의 경우 사용성 문제로 Refresh Token을 같이 사용해야 한다. <br>
따라서 서버에서 상태를 저장하는 단점은 Session과 같다.

### 성능

Scale-out 상황에서 상태 관리를 위해 글로벌 스토리지를 쓸 경우 Refresh Token은 가끔 접근해도 되는 반면, Session은 매 번 접근해야 한다. <br>
따라서 네트워크 비용이 더 적은 JWT를 이용한 방식의 성능이 더 뛰어나다.

### 선택 

JWT가 성능상 이점이 있다고 판단되지만 보안상의 문제를 커버할만큼 뛰어나게 성능이 좋은지는 확실하지 않다. <br>
추후 성능 목표를 잡고 Session 방식으로 인해 목표 달성이 어렵다 느껴질 경우에 한해 JWT 방식으로의 전환을 고려해보면 좋을 것 같다.

</details>

<details>
<summary>로그인 체크 Filter vs Interceptor vs AOP</summary>

### 필터

- 제외 경로를 지정하기 위해서는 추가적인 구현 코드가 필요하다.
- 예외가 발생했을 때 ControllerAdvice에서 처리해줄 수 없어 일관된 예외 처리 하기가 어렵다.

### AOP

- URL Path 단위로 로그인을 체크하고 싶을 때 구현이 번거롭다. 직접 Request 객체로부터 URL를 꺼내고 작업해야 한다.

### 선택

위와 같은 문제가 없는 인터셉터를 사용한다.
</details>

<details>
<summary>Scale-out 상황에서 세션 관리를 어떻게 할까? </summary>

## Sticky Session vs Session Clustering vs Global Session Storage

### Sticky Session

서버 하나로 부하가 몰릴 수 있는 문제가 있다.

### Session Clustering

서버가 늘어날수록 각 서버 간에 세션 동기화를 위한 네트워크 비용 또한 증가한다. <br>
유저가 늘어날수록 유지하는 세션으로 인해 서버의 메모리가 부족해진다. <br>
따라서 확장성이 떨어지는 문제가 있다.

### 선택
위와 같은 문제가 없는 Global Session Storage를 사용한다. <br>
Global Session Storage의 경우 추가적인 컴포넌트로 인해 시스템이 복잡해진다는 문제가 있다. <br>
어쩔 수 없는 부분이라 생각되고 '확장성', '추후 캐시 저장소로도 사용할 수 있다는 점'과 트레이드오프하자.

</details>

<details>
<summary>세션 저장소 Redis vs Memcached </summary>

### 후보 선정

세션은 영구적인 데이터 저장이 필요하지 않다. 따라서 In-Memoery DB를 활용하여 가능한 빠르게 데이터에 접근하는 것이 효율적이다. <br>
또 세션은 key-value 형식으로 저장하기에 알맞다. 따라서 In-Memory DB 중 key-value 형태로 저장하는 Redis와 Memcached를 후보로 둘 수 있다.

세션 저장소로서 비교해 볼 포인트는 '성능'과 '장애' 관련 부분이다.

### 성능

둘 다 1ms 이하의 응답하며, 2016년 자료에 따르면 초당 10만개의 작업을 처리할 수 있다.
한 편 한번에 트래픽이 몰리게 될 경우 Redis는 응답속도가 불안정 해진다는 문제가 있다.


### 장애 복구

Redis는 장애가 일어나도 데이터를 보존할 수 있다. 예를 들어 RDB 방식으로 Redis 메모리 내에 있는 데이터들의 스냅샷을 찍어 디스크에 저장하거나, AOF 방식으로 현재까지 수행된 모든 Write 연산을 디스크에 저장하면 된다. 반면 Memcached는 데이터를 백업하는 기능이 없어 장애가 일어날 경우 모든 데이터가 사라지게 된다.

### 선택

세션 저장소로써의 각 장단점을 사용자 관점에서 생각해보자. <br>
Memcached는 중간에 사용자가 사이트를 사용 중 로그인이 풀릴 수 있다. <br>
만약 결제하는 과정이었다면 결제하는 사람의 신원을 알 수 없게 되어 결제에 실패할 수 있다. <br>

반면 Redis는 대규모 트래픽 발생 시 여러 요청들이 간헐적으로 느리게 처리될 수 있다. 

로그인이 풀리는 현상이 더 사용성이 좋지 못하다고 판단되므로 **Redis를 세션 저장소로 사용**한다.

</details>

<details>
<summary>세션 저장소에 저장할 LoginUser 객체를 어떤 방식으로 직렬화 해야할까?</summary>

자바 직렬화는 용량이 커진다는 문제가 있다.<br>
Json 데이터에 비해 최소 2배가 커지고 Redis는 In-Memory DB로써 용량에 민감하다.<br>
따라서 상대적으로 보편적이면서 용량 문제도 없는 Json 방식을 이용해 직렬화한다.

</details>

## 상품

#### UseCase
* 관리자는 상품을 '**등록**', '**수정**', '**삭제**' 할 수 있다.
* 사용자는 '**하나의 상품을 조회**' 할 수 있다.
* 사용자는 카테고리 별로 '**여러 상품을 조회**' 할 수 있다.
* 사용자는 가격, 신상품 순으로 '**여러 상품을 정렬해 조회**'할 수 있다.

#### 고민

<details>
<summary> ~외 4종과 같은 상품들은 어떤 구조를 만들어서 처리하지? </summary>

‘전시용 상품’(DisplayProduct)이라는 테이블을 새로 만든다. <br>
그 후 '전시용 상품'과 '상품'(Product) 테이블을 일대다 관계로 맺어준다. <br>
유저에게 보이는 상품은 '전시용 상품' 테이블에 들어 있는 상품들이며 실제로 구매하게 되는 상품은 '상품' 테이블에 있는 상품이 된다.

[관련 글](https://velog.io/@sontulip/how-to-db-design#:~:text=%EB%A7%8C%EB%93%A4%EC%96%B4%EC%84%9C%20%EC%B2%98%EB%A6%AC%ED%95%A0%20%EA%B2%83%EC%9D%B8%EA%B0%80%3F-,%EB%AC%B8%EC%A0%9C,-%EB%A7%88%EC%BC%93%EC%BB%AC%EB%A6%AC%EB%A5%BC%20%EB%B3%B4%EB%A9%B4%20%EB%8B%A4%EC%9D%8C%EA%B3%BC)

</details>

<details>
<summary> 만약 카테고리가 3,4,5뎁스로 많아진다면 어떡하지? </summary>

'전시용 상품' 테이블에서 '카테고리' 테이블을 따로 뺀 후 하나의 카테고리가 부모 카테고리와 연관되도록 자가참조 관계를 맺어주자.<br>
새로운 카테고리가 추가된다면 '카테고리' 테이블에 새로운 카테고리를 추가하기만 하면 된다. <br>
‘전시용 상품’ 테이블에 있는 각각의 상품은 뎁스 최하위의 카테고리만 갖고 있으면 '부모 카테고리 id' 필드를 통하여 연관된 모든 카테고리를 찾아낼 수 있다.

[관련 글](https://velog.io/@sontulip/how-to-db-design#:~:text=%EC%96%B4%EB%96%BB%EA%B2%8C%20%ED%95%A0%20%EA%B2%83%EC%9D%B8%EA%B0%80%3F-,%EB%AC%B8%EC%A0%9C,-%ED%98%84%EC%9E%AC%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EB%8A%94%20%E2%80%98%EC%A0%84%EC%8B%9C%EC%9A%A9)
</details>

[상품 저장 도중 이미지 저장에 실패하면 저장된 이미지는 어떻게 롤백 처리를 하지?](https://velog.io/@sontulip/s3-rollback-problem)<br>
(관련 코드 : https://github.com/sgo8308/Market-Gola-Batch)

## 주문

#### UseCase
* 사용자 원하는 상품을 '**주문**'할 수 있다.

#### 고민

<details>
<summary>여러 사람이 동시에 주문할 경우 동시성 문제를 어떻게 해결할까?</summary>

### 문제 상황
여러 명이 동시에 상품 주문을 진행하게 될 경우, 실제 주문량보다 적은 수가 재고에서 차감되는 문제가 발생할 수 있다. 이렇게 될 경우 주문에 성공한 사용자는 시간이 지나고 나서야 재고가 없다는 사실을 알게 되고, 이는 사용자 경험을 크게 떨어뜨린다.

### 원인
주문 로직을 수행하는 두 개 이상의 트랜잭션이 재고에 대한 데이터를 동시에 조회하는 것이 원인이다.

### 비관적 락 

장점
* 구현이 간단하다.

단점
* 데드락에 걸릴 위험이 있고, 락을 걸고 해제하는 과정의 오버헤드로 성능 저하가 있을 수 있다.
* DB Replication이 진행된 경우에는 사용할 수 없다. 서로 다른 DB에서 읽기를 진행할 수 있으므로.

### 낙관적 락

장점
* 동시에 주문하는 경우가 적다면 락으로 인한 오버헤드가 없으므로 성능이 좋다. 

단점
* 동시에 주문하는 경우가 많을 때는 업데이트 실패 후 다시 시도하는 과정이 오래 걸리므로 성능이 떨어진다. 
* DB Replication이 진행된 경우 쓰기용 Master DB가 여러개라면 낙관적 락으로 문제를 해결하기가 어렵다.


### 분산 락

공통 장점
* 락을 걸 대상이 존재하지 않을 때도 사용 가능하다. 
* 분산 DB 환경에서도 사용 가능하다.

### MySQL의 네임드 락을 이용한 분산 락

장점
* 추가적인 컴포넌트가 필요 없으므로 비용이 적고 시스템 복잡도가 낮다.

단점
* 락 획득용 커넥션 풀을 따로 만들어야 하고 이 때문에 구현이 꽤 복잡하다.

### Redis와 Redisson Client를 이용한 분산 락

장점
* MySQL을 이용한 네임드 락보다 성능상 뛰어나다고 하지만 이 부분은 실제와 가까운 환경에서 성능 테스트를 통해 비교해봐야 할 듯하다.

단점
* 추가적으로 Redis를 구축하고 운영하는 비용이 든다.
* 락을 잡은 채로 영원히 놓지 않을 수 있기 때문에 타임아웃 시간을 잡아야 한다. 이로 인해 잠깐의 지연 문제로 락이 풀린 상태에서 그대로 로직을 진행하게 되면 동시성 문제가 여전히 발생할 수 있다. 이를 해결하기 위해 낙관적 락을 같이 써야할 수도 있다.

### 언제 어떤 것을 쓸까?

동시성 문제가 자주 발생하지 않는 상황 <br>
-> 성능상 가장 좋은 낙관적 락을 사용

동시성 문제가 자주 발생하는 상황 <br>
-> 간단한 프로그램이라면 비관적 락 사용 <br>
-> DB Replication할 정도로 큰 프로그램이라면 분산락 사용 <br>
-> Redis를 구축할 비용이 없고 성능적으로 요구사항이 크지 않다면 MySQL로 분산락 구현 <br>
-> 성능이 중요하다면 Redis로 분산락 구현

### 결론
서비스가 이제 시작하는 상황이라고 가정할 때 하나의 상품을 동시에 주문하는 경우는 매우 드물게 일어난다고 판단된다. 또 어느 정도 큰 서비스인 마켓 컬리에서도 초당 10개의 상품을 판매하고 있고, 이것을 전체 상품 갯수 1만개로 나누면 각 상품은 초당 0.001개가 판매되므로 동시성 문제가 자주 발생하지 않는 것으로 보인다.

따라서 동시성 문제가 자주 발생하지 않는 상황에서 성능이 좋은 낙관적 락을 사용하여 구현하자.

</details>

[왜 운영 DB인 MySQL을 사용할 때는 낙관적 락 테스트 도중 무한 루프가 발생하지?](https://velog.io/@sontulip/optimistic-lock-infinite-loop)

# 화면 설계

[kakao oven](https://ovenapp.io/view/pDhwkCQw9govPoESRQGitWNOUVO9zyaK/)


![화면 설계](https://user-images.githubusercontent.com/71138398/197139145-ac079914-830d-4cdf-a84c-643fb3238ce4.svg)

# API 문서
[swagger](https://app.swaggerhub.com/apis-docs/sgo8308/market-gola2/1.0.0)

![api doc](https://user-images.githubusercontent.com/71138398/197344447-b93a8eb9-ecac-4f4a-809e-ea99b8bce067.png)
# DB ERD
[설계 과정](https://velog.io/@sontulip/how-to-db-design)


![ERD 최종](https://user-images.githubusercontent.com/71138398/172528406-28439eab-715b-4e8c-88ac-506709659d2f.png)
