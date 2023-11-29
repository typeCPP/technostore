package com.technostore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Integer count;

    @ManyToOne(targetEntity = OrderEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_id")
    private OrderEntity order;
}
