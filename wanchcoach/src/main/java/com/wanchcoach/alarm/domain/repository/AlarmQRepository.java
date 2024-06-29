package com.wanchcoach.alarm.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.alarm.domain.service.dto.DrugAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.wanchcoach.alarm.domain.entity.QDrugAdministrationTime.drugAdministrationTime;
import static com.wanchcoach.alarm.domain.entity.QFamily.family;
import static com.wanchcoach.alarm.domain.entity.QMember.member;
import static com.wanchcoach.alarm.domain.entity.QPrescription.prescription;
import static com.wanchcoach.alarm.domain.entity.QTreatment.treatment;


@Repository
@RequiredArgsConstructor
public class AlarmQRepository {

    private final JPAQueryFactory queryFactory;

    public List<DrugAlarm> getNeedDrugAlarm(LocalTime time){
        return queryFactory.select(Projections.constructor(DrugAlarm.class,
                    family.familyId,
                    family.name
//                    ,member.device_token
                ))
                .from(drugAdministrationTime)
                .join(member).on(member.memberId.eq(drugAdministrationTime.member.memberId))
                .join(family).on(family.member.memberId.eq(member.memberId))
                .join(treatment).on(treatment.family.familyId.eq(family.familyId))
                .join(prescription).on(prescription.prescriptionId.eq(treatment.prescription.prescriptionId))
                .where((drugAdministrationTime.morning.eq(time).and(prescription.morning.eq(true)).and(prescription.taking.eq(true)))
                        .or(drugAdministrationTime.noon.eq(time).and(prescription.noon.eq(true)).and(prescription.taking.eq(true)))
                        .or(drugAdministrationTime.evening.eq(time).and(prescription.evening.eq(true)).and(prescription.taking.eq(true)))
                        .or(drugAdministrationTime.beforeBed.eq(time).and(prescription.beforeBed.eq(true)).and(prescription.taking.eq(true))))
                .groupBy(family.familyId)
                .fetch();

    }

    public List<DrugAlarm> getTodayTreatment(LocalDateTime time){
        return queryFactory.select(Projections.constructor(DrugAlarm.class,
                        family.familyId,
                        family.name
//                    ,member.device_token
                ))
                .from(drugAdministrationTime)
                .join(member).on(member.memberId.eq(drugAdministrationTime.member.memberId))
                .join(family).on(family.member.memberId.eq(member.memberId))
                .join(treatment).on(treatment.family.familyId.eq(family.familyId))
                .where(treatment.taken.eq(false)
                        .and(treatment.date.year().eq(time.getYear()))
                        .and(treatment.date.month().eq(time.getMonthValue()))
                        .and(treatment.date.dayOfMonth().eq(time.getDayOfMonth())))
                .groupBy(family.familyId)
                .fetch();
    }

    public List<DrugAlarm> getTomorrowTreatment(LocalDateTime time){

        LocalDate tomorrowDate = time.toLocalDate().plusDays(1);
        LocalDateTime tomorrowStart = tomorrowDate.atStartOfDay();
        LocalDateTime tomorrowEnd = tomorrowDate.plusDays(1).atStartOfDay().minusNanos(1);

        return queryFactory.select(Projections.constructor(DrugAlarm.class,
                        family.familyId,
                        family.name
//                    ,member.device_token
                ))
                .from(drugAdministrationTime)
                .join(member).on(member.memberId.eq(drugAdministrationTime.member.memberId))
                .join(family).on(family.member.memberId.eq(member.memberId))
                .join(treatment).on(treatment.family.familyId.eq(family.familyId))
                .where(treatment.taken.eq(false)
                    .and(treatment.date.goe(tomorrowStart))
                    .and(treatment.date.loe(tomorrowEnd)))
                .groupBy(family.familyId)
                .fetch();
    }
}
