package com.thesys.titan.user.entity;

import jakarta.persistence.Entity;
import lombok.Setter;
import lombok.Getter;
import com.thesys.titan.user.dto.UserRequest;
import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import com.thesys.titan.common.entity.BaseEntity;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "use_yn", columnDefinition = "TINYINT(1)")
    private Boolean useYn;

    public static User of(UserRequest request) {
        return User.builder()
            .id(request.getId())
            .username(request.getUsername())
            .password(request.getPassword())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .useYn(request.getUseYn())
            .build();
    }

    public void update(UserRequest request) {
        this.id = request.getId();
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.name = request.getName();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.useYn = request.getUseYn();
    }
}
