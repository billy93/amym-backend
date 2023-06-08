package com.atibusinessgroup.amanyaman.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Country.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "country_iata_code")
    private Integer countryIataCode;

    @Column(name = "post_code")
    private Integer postCode;

    @Column(name = "associated_airport")
    private String associatedAirport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public Country countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Country countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Country currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getCountryIataCode() {
        return countryIataCode;
    }

    public Country countryIataCode(Integer countryIataCode) {
        this.countryIataCode = countryIataCode;
        return this;
    }

    public void setCountryIataCode(Integer countryIataCode) {
        this.countryIataCode = countryIataCode;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public Country postCode(Integer postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    public String getAssociatedAirport() {
        return associatedAirport;
    }

    public Country associatedAirport(String associatedAirport) {
        this.associatedAirport = associatedAirport;
        return this;
    }

    public void setAssociatedAirport(String associatedAirport) {
        this.associatedAirport = associatedAirport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
                "id=" + getId() +
                ", countryName='" + getCountryName() + "'" +
                ", countryCode='" + getCountryCode() + "'" +
                ", currencyCode='" + getCurrencyCode() + "'" +
                ", countryIataCode=" + getCountryIataCode() +
                ", postCode=" + getPostCode() +
                ", associatedAirport='" + getAssociatedAirport() + "'" +
                "}";
    }
}
