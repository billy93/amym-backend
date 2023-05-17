package com.atibusinessgroup.amanyaman.service.dto;

import java.util.List;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserTravelAgentDTO extends UserDTO{

    private TravelAgentDTO travelAgent;

    public TravelAgentDTO getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(TravelAgentDTO travelAgent) {
        this.travelAgent = travelAgent;
    }
}
