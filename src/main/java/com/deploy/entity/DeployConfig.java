package com.deploy.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "DEPLOY_CONFIG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeployConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPLOY_CONFIG_ID")
    private Long id;



}
