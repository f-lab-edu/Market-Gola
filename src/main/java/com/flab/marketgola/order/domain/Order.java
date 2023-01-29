package com.flab.marketgola.order.domain;

import com.flab.marketgola.common.domain.BaseEntity;
import com.flab.marketgola.user.domain.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "`order`")
@DynamicInsert // 필드가 Null일 경우 삽입시 제외
@NoArgsConstructor
@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String receiverName;

    @Column(length = 11, nullable = false)
    private String receiverPhone;

    @Column(length = 100, nullable = false)
    private String receiverAddress;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Order(Long id, String receiverName, String receiverPhone, String receiverAddress,
            OrderStatus orderStatus, LocalDateTime deliveredAt, User user) {
        this.id = id;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.orderStatus = orderStatus;
        this.deliveredAt = deliveredAt;
        this.user = user;
    }
}
