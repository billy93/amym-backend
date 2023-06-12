package com.atibusinessgroup.amanyaman.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

/**
 * A City.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_mapping")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "code")
    private String code;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "curr_id")
    private String currId;

    @Column(name = "value")
    private String value;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_brochure")
    private String productBrochure;

    @Column(name = "product_personal_accident_cover")
    private String productPersonalAccidentCover;
    @Column(name = "product_medical_cover")
    private String productMedicalCover;
    @Column(name = "product_travel_cover")
    private String productTravelCover;

    @ManyToOne
    private TravellerType travellerType;
    @ManyToOne
    private BandType bandType;
    @ManyToOne
    private AreaGroup areaGroup;
    @ManyToOne
    private PlanType planType;
    @ManyToOne
    private Product productAdditionalWeek;

    @ManyToOne
    private DocumentType benefitDoc;
    @ManyToOne
    private DocumentType wordingDoc;
    @ManyToOne
    private DocumentType covidDoc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
