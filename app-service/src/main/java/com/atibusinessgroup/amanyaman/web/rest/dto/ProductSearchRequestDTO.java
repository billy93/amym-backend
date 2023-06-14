package com.atibusinessgroup.amanyaman.web.rest.dto;

public class ProductSearchRequestDTO {
    private String productCode;
    private String travellerType;
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
}
