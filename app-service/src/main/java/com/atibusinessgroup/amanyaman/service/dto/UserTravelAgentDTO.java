package com.atibusinessgroup.amanyaman.service.dto;

import com.atibusinessgroup.amanyaman.domain.Authority;
import com.atibusinessgroup.amanyaman.domain.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserTravelAgentDTO extends UserDTO{

    private TravelAgentDTO travelAgent;

    public UserTravelAgentDTO(){

    }

    public UserTravelAgentDTO(User u) {
        super(u);

        if(u.getTravelAgent() != null){
            TravelAgentDTO travelAgentDTO = new TravelAgentDTO();
            travelAgentDTO.setId(u.getTravelAgent().getId());
            travelAgentDTO.setTravelAgentName(u.getTravelAgent().getTravelAgentName());
            setTravelAgent(travelAgentDTO);
        }
    }

    public TravelAgentDTO getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(TravelAgentDTO travelAgent) {
        this.travelAgent = travelAgent;
    }
}
