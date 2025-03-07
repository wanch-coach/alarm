package com.wanchcoach.alarm.domain.entity;

import com.wanchcoach.alarm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 회원 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String encryptedPwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean loginType;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private boolean locationPermission;

    @Column(nullable = false)
    private boolean callPermission;

    @Column(nullable = false)
    private boolean cameraPermission;

    @Transient
    private OAuthProvider oAuthProvider;

}
