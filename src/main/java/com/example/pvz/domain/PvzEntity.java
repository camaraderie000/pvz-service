package com.example.pvz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pvz")
@Getter
@Setter
@NoArgsConstructor
public class PvzEntity {
    @Id
    private UUID id;
    @Column(name = "registration_date") private OffsetDateTime registrationDate;
    private String city;
}