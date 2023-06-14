package com.atibusinessgroup.amanyaman.web.rest.dto;

public class TravelAgentSearchRequestDTO {
    private String travelAgentName;
    private String custcode;

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }
}
