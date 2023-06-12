package com.atibusinessgroup.amanyaman.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A City.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_travel_agent")
public class ProductTravelAgent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "npwp")
    private Boolean npwp;

    @Column(name = "premium_price", precision = 21, scale = 2)
    private BigDecimal premiumPrice;

    @Column(name = "commision_lv_1", precision = 21, scale = 2)
    private BigDecimal commisionLv1;

    @Column(name = "commision_lv_2", precision = 21, scale = 2)
    private BigDecimal commisionLv2;

    @Column(name = "commision_lv_3", precision = 21, scale = 2)
    private BigDecimal commisionLv3;

    @Column(name = "total_commision", precision = 21, scale = 2)
    private BigDecimal totalCommision;

    @Column(name = "after_commision_price", precision = 21, scale = 2)
    private BigDecimal afterCommisionPrice;

    @Column(name = "ppn", precision = 21, scale = 2)
    private BigDecimal ppn;

    @Column(name = "pph_23", precision = 21, scale = 2)
    private BigDecimal pph23;

    @Column(name = "ppn_value", precision = 21, scale = 2)
    private BigDecimal ppnValue;

    @Column(name = "pph_23_value", precision = 21, scale = 2)
    private BigDecimal pph23Value;

    @Column(name = "aji_price", precision = 21, scale = 2)
    private BigDecimal ajiPrice;

    @ManyToOne
    // @JsonIgnoreProperties(value = "travelAgentProducts", allowSetters = true)
    private Product product;

    @ManyToOne
    // @JsonIgnoreProperties(value = "travelAgentProducts", allowSetters = true)
    private TravelAgent travelAgent;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductTravelAgent)) {
            return false;
        }
        return id != null && id.equals(((ProductTravelAgent) o).id);
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
