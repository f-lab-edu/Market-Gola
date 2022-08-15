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
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='배송지 주소를 저장하는 테이블'