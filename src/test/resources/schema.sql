-- Spring Boot가 자동으로 실행하는 DDLScript
-- 테스트용 메모리DB의 Schema를 만들어준다
CREATE TABLE `membership`
(
    `id`         tinyint       NOT NULL AUTO_INCREMENT,
    `level`      varchar(45)   NOT NULL,
    `point_rate` decimal(6, 3) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `level_UNIQUE` (`level`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='멤버십 등급에 대한 테이블';

CREATE TABLE `user`
(
    `id`            int unsigned           NOT NULL AUTO_INCREMENT,
    `login_id`      varchar(30)            NOT NULL,
    `password`      varchar(256)           NOT NULL,
    `name`          varchar(30)            NOT NULL,
    `email`         varchar(30)            NOT NULL,
    `phone_number`  varchar(11)            NOT NULL,
    `gender`        enum ('MALE','FEMALE') NOT NULL,
    `birth`         date                            DEFAULT NULL,
    `created_at`    datetime               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    datetime               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `membership_id` tinyint                NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `login_id_UNIQUE` (`login_id`),
    UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    KEY `fk_membership_idx` (`membership_id`),
    CONSTRAINT `fk_membership` FOREIGN KEY (`membership_id`) REFERENCES `membership` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='회원 정보를 저장하는 테이블';

CREATE TABLE `shipping_address`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `address`    varchar(100) NOT NULL,
    `is_default` tinyint      NOT NULL,
    `user_id`    int unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id_idx` (`user_id`),
    CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='배송지 주소를 저장하는 테이블';

CREATE TABLE `product_category`
(
    `id`        int unsigned NOT NULL AUTO_INCREMENT,
    `name`      varchar(30)  NOT NULL,
    `parent_id` int unsigned DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`),
    KEY `product_category_product_category_id_idx` (`parent_id`),
    CONSTRAINT `product_category_product_category_id` FOREIGN KEY (`parent_id`) REFERENCES `product_category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='카테고리 테이블';

CREATE TABLE `display_product`
(
    `id`                     bigint unsigned NOT NULL AUTO_INCREMENT,
    `name`                   varchar(100)    NOT NULL,
    `description_image_name` varchar(300)    NOT NULL,
    `main_image_name`        varchar(300)    NOT NULL,
    `created_at`             datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`             datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `product_category_id`    int unsigned    NOT NULL,
    PRIMARY KEY (`id`),
    KEY `display_product_product_category_id_idx` (`product_category_id`),
    CONSTRAINT `display_product_product_category_id` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='전시용 상품 테이블';

CREATE TABLE `product`
(
    `id`                 bigint unsigned   NOT NULL AUTO_INCREMENT,
    `name`               varchar(256)      NOT NULL,
    `price`              int unsigned      NOT NULL,
    `stock`              smallint unsigned NOT NULL,
    `created_at`         datetime          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         datetime          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`         tinyint           NOT NULL DEFAULT '0',
    `display_product_id` bigint unsigned   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `display_product_id_idx` (`display_product_id`),
    CONSTRAINT `product_display_product_id` FOREIGN KEY (`display_product_id`) REFERENCES `display_product` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='상품 테이블';

CREATE TABLE `order`
(
    `id`               bigint unsigned                                         NOT NULL AUTO_INCREMENT,
    `receiver_name`    varchar(30)                                             NOT NULL,
    `receiver_phone`   varchar(11)                                             NOT NULL,
    `receiver_address` varchar(100)                                            NOT NULL,
    `order_status`     enum ('PROCESSING','DELIVERING','DELIVERED','CANCELED') NOT NULL DEFAULT 'PROCESSING',
    `created_at`       datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       datetime(6)                                             NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `delivered_at`     datetime                                                         DEFAULT NULL,
    `user_id`          int unsigned                                            NOT NULL,
    PRIMARY KEY (`id`),
    KEY `user_idx` (`user_id`),
    CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='주문 테이블';

CREATE TABLE `order_product`
(
    `id`         bigint unsigned NOT NULL AUTO_INCREMENT,
    `count`      int unsigned    NOT NULL,
    `order_id`   bigint unsigned NOT NULL,
    `product_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `product_idx` (`product_id`),
    KEY `order_idx` (`order_id`),
    CONSTRAINT `order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='주문 상품 테이블';



