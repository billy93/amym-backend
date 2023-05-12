package com.atibusinessgroup.amanyaman.domain;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * A TravelAgent.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_agent")
public class TravelAgent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "travel_agent_name")
    private String travelAgentName;

    @Column(name = "travel_agent_phone")
    private String travelAgentPhone;

    @Column(name = "travel_agent_email")
    private String travelAgentEmail;

    @Column(name = "travel_agent_address")
    private String travelAgentAddress;

    @Column(name = "commission")
    private String commission;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "custcode")
    private String custcode;
    
    @Column(name = "api_password")
    private String apiPassword;

    @Column(name = "custid")
    private String custid;

    @Column(name = "cgroup")
    private String cgroup;

    @Column(name = "legal_name")
    private String legalName;
    
    @Column(name = "allow_credit_payment")
    private boolean allowCreditPayment;

    @ManyToOne
    private City city;

    // @OneToMany(mappedBy = "travelAgent", fetch = FetchType.EAGER)
    // private Set<TravelAgentProduct> travelAgentProducts = new HashSet<>();

    @Column(name = "proforma_invoice_recipients")
    private String proformaInvoiceRecipients;

	@Override
    public String toString() {
        return "TravelAgent{" +
            "id=" + id +
            ", travelAgentName='" + travelAgentName + '\'' +
            ", travelAgentPhone='" + travelAgentPhone + '\'' +
            ", travelAgentEmail='" + travelAgentEmail + '\'' +
            ", travelAgentAddress='" + travelAgentAddress + '\'' +
            ", commission='" + commission + '\'' +
            ", paymentType='" + paymentType + '\'' +
            ", custcode='" + custcode + '\'' +
            ", custid='" + custid + '\'' +
            ", allowCreditPayment=" + allowCreditPayment +
            // ", travelAgentProducts=" + travelAgentProducts +
            '}';
    }

	public String getApiPassword() {
		return apiPassword;
	}

	public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	}
}
