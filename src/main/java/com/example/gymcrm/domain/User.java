package com.example.gymcrm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Entity
@Table(name = "`users`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @ToString.Exclude
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @ToString.Exclude
    private String lastName;

    @Column(nullable = false, unique = true)
    @ToString.Exclude
    private String username;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}
