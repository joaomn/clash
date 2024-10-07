package br.com.clashapi.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "battle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Battle {

	@Id
	private String id;

	@Field("battleTime")
	private String battleTime;

	private Player winner;
	private Player loser;
}
