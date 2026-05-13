package com.powerstrike.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String schedule;

    @Column(nullable = false)
    private Double monthlyCost;

    @Column(nullable = false)
    private boolean active = true;
}
