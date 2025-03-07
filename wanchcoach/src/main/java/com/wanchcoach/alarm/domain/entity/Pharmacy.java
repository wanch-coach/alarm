package com.wanchcoach.alarm.domain.entity;

import com.wanchcoach.alarm.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 약국 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "pharmacy")
public class Pharmacy extends BaseEntity {

    @Id
    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(precision = 15, scale = 12)
    private BigDecimal longitude;

    @Column(precision = 15, scale = 13)
    private BigDecimal latitude;

    @Column
    private String postCdn;

    @Column(length=2000)
    private String etc;

    @Column(nullable = false)
    private String hpid;
}
