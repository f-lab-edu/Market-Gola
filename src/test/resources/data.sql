-- Spring Boot가 자동으로 실행해주는 DMLScript
-- 테스트DB용 사전 데이터를 넣어준다.

-- 유저 서비스용 초기 데이터
-- black 멤버십이 등록된다.
insert into membership (level, point_rate)
values ('black', 0.5);

-- 상품 서비스용 초기 데이터
insert into product_category (name)
values ('과일');

insert into product_category (name)
values ('채소');
