package com.atibusinessgroup.amanyaman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AreaGroup.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "area_group")
public class AreaGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_group_name")
    private String areaGroupName;

    @Column(name = "area_group_description", columnDefinition = "TEXT")
    private String areaGroupDescription;

}
