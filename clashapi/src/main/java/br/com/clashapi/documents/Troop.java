package br.com.clashapi.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "troop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Troop {
    @Id
    private String id; // ID da carta
    private Team team;
}
