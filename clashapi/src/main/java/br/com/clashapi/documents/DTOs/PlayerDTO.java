package br.com.clashapi.documents.DTOs;

import br.com.clashapi.documents.Cards;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private String tag;         // Tag do jogador
    private String clanTag;     // Tag do clan
    private Cards cards; // Lista de IDs de cartas (se necessário)
}
