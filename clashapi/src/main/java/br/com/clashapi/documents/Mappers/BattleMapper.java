package br.com.clashapi.documents.Mappers;

import br.com.clashapi.documents.Battle;
import br.com.clashapi.documents.Player;
import br.com.clashapi.documents.DTOs.BattleDTO;
import br.com.clashapi.documents.DTOs.PlayerDTO;

public class BattleMapper {


	    public static BattleDTO toDTO(Battle battle) {
	        BattleDTO battleDTO = new BattleDTO();
	        battleDTO.setBattleTime(battle.getBattleTime());

	        // Map Winner
	        PlayerDTO winnerDTO = toPlayerDTO(battle.getWinner());
	        battleDTO.setWinner(winnerDTO);

	        // Map Loser
	        PlayerDTO loserDTO = toPlayerDTO(battle.getLoser());
	        battleDTO.setLoser(loserDTO);

	        return battleDTO;
	    }

	    private static PlayerDTO toPlayerDTO(Player player) {
	        if (player == null) {
	            return null; // Retornar null se o jogador não existir
	        }
	        
	        PlayerDTO playerDTO = new PlayerDTO();
	        playerDTO.setTag(player.getTag());
	        playerDTO.setClanTag(player.getClan() != null ? player.getClan().getTag() : null); // Verifica se o clan não é nulo
	        playerDTO.setCards(player.getCards()); // Certifique-se de que este método retorna uma lista de IDs de cartas
	        return playerDTO;
	    }
}
