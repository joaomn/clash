package br.com.clashapi.documents.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BattleDTO {

	private String battleTime;
	private PlayerDTO winner;
	private PlayerDTO loser;
}
