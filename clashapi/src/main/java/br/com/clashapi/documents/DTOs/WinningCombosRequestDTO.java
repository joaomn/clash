package br.com.clashapi.documents.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WinningCombosRequestDTO {

	private int size;
    private int percentage;
    private String startTime;
    private String endTime;
}
