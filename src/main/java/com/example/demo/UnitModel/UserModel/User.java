package com.example.demo.UnitModel.UserModel;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "UserData")
@Data

public class User implements UserDetails {

    @Id

    @Column(nullable = false)
    private String id;

    @Column(nullable = true)
    private String avatarUrl;
    @Column(nullable = false, unique = true)
    @NotNull(message = "Никнейм обязателен")
    @Size(min = 3, max = 99, message = "Имя должно быть от 3 до 99 символов")
    private String nickname;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String family;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль не менее 8 символов")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Возраст обязателен")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 100, message = "Возраст должен быть меньше 120")
    private Integer age;
    @Column(nullable = true, name = "userStatus")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Override
    public String getUsername() {
        return this.nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
}