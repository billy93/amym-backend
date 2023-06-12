package com.atibusinessgroup.amanyaman.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A City.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "band_type")
public class BandType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "travel_duration_name")
    private String travelDurationName;

    @Column(name = "travel_duration_description")
    private String travelDurationDescription;

    @Column(name = "start")
    private int start;
    
    @Column(name = "end")
    private int end;

    @Override
    public Instant getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    public String getLastModifiedBy() {
        return super.getLastModifiedBy();
    }

    @Override
    public Instant getLastModifiedDate() {
        return super.getLastModifiedDate();
    }

    @Override
    public String getCreatedBy() {
        return super.getCreatedBy();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BandType)) {
            return false;
        }
        return id != null && id.equals(((BandType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BandType{" +
            "id=" + getId() +
            ", travelDurationName='" + getTravelDurationName() + "'" +
            ", travelDurationDescription='" + getTravelDurationDescription() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            
            "}";
    }
}
