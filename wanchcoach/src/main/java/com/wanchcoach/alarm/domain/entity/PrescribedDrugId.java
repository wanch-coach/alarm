package com.wanchcoach.alarm.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PrescribedDrugId implements Serializable {
    private Long prescription;
    private Long drug;
}
