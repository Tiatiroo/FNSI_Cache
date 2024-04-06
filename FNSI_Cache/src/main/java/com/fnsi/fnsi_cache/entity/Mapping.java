package com.fnsi.fnsi_cache.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mappings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "\"system\"")
    private String system;
    private String version;
    private String code;
    private String display;

}



