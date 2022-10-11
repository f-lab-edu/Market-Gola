-- Spring Boot가 자동으로 실행해주는 DMLScript
-- 테스트DB용 사전 데이터를 넣어준다.

-- 유저 서비스용 초기 데이터
-- black 멤버십이 등록된다.
insert into membership (level, point_rate)
values ('black', 0.5);

-- 상품 서비스용 초기 데이터
-- 과일 카테고리, 전시용 상품 하나와 그에 연관된 실제 상품 3개가 등록된다.
-- 실제 상품 2개는 서로 이름 가격, 수량이 다르고 나머지 하나는 삭제된 상태이다.
insert into product_category (name)
values ('과일');

insert into display_product (name, description_image_name, main_image_name, product_category_id)
values ('친환경 손질 유러피안 샐러드 6종',
        'e662963a-42dc-11ed-b878-0242ac120002.jpg',
        'eed16cce-42dc-11ed-b878-0242ac120002.jpg',
        1);

insert into product (name, price, stock, is_deleted, display_product_id)
values ('친환경 손질 유러피안 믹스', 1000, 10, false, 1);

insert into product (name, price, stock, is_deleted, display_product_id)
values ('친환경 손질 프레시 믹스', 2000, 8, false, 1);

insert into product (name, price, stock, is_deleted, display_product_id)
values ('친환경 손질 코리안 믹스', 3000, 0, true, 1);