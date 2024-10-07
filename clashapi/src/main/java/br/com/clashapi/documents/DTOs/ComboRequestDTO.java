package br.com.clashapi.documents.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboRequestDTO {
	
	private List<Integer> cardIds;
    private String startTime;
    private String endTime;

}
