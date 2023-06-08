package com.atibusinessgroup.amanyaman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AreaGroup.
 */
@Entity
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaGroupName() {
        return areaGroupName;
    }

    public AreaGroup areaGroupName(String areaGroupName) {
        this.areaGroupName = areaGroupName;
        return this;
    }

    public void setAreaGroupName(String areaGroupName) {
        this.areaGroupName = areaGroupName;
    }

    public String getAreaGroupDescription() {
        return areaGroupDescription;
    }

    public AreaGroup areaGroupDescription(String areaGroupDescription) {
        this.areaGroupDescription = areaGroupDescription;
        return this;
    }

    public void setAreaGroupDescription(String areaGroupDescription) {
        this.areaGroupDescription = areaGroupDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaGroup)) {
            return false;
        }
        return id != null && id.equals(((AreaGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaGroup{" +
            "id=" + getId() +
            ", areaGroupName='" + getAreaGroupName() + "'" +
            ", areaGroupDescription='" + getAreaGroupDescription() + "'" +
            "}";
    }
}
