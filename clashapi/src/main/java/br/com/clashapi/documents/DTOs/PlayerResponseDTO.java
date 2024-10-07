package br.com.clashapi.documents.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerResponseDTO {
    private String tag;
    private String name;
    private Integer expPoints;
    private Integer starPoints;
}
