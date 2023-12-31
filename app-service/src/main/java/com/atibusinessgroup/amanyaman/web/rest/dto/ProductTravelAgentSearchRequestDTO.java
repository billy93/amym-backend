package com.atibusinessgroup.amanyaman.web.rest.dto;

public class ProductTravelAgentSearchRequestDTO {
    private String productCode;
    private String travellerType;
    private String travelAgent;
    private String bandType;
    private String areaGroup;
    private String planType;

    public String getAreaGroup() {
        return areaGroup;
    }
    public String getBandType() {
        return bandType;
    }
    public String getProductCode() {
        return productCode;
    }
    public String getTravellerType() {
        return travellerType;
    }
    public String getTravelAgent() {
        return travelAgent;
    }
    public void setAreaGroup(String areaGroup) {
        this.areaGroup = areaGroup;
    }
    public void setBandType(String bandType) {
        this.bandType = bandType;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }
    public String getPlanType() {
        return planType;
    }
    public void setPlanType(String planType) {
        this.planType = planType;
    }
    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }
}
