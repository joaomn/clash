package br.com.clashapi.documents.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialConditionWinsRequestDTO {
    private int cardId;                 
    private int percentageLessTrophies;  
    private String startTime;           
    private String endTime;              
}