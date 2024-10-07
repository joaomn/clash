package br.com.clashapi.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clashapi.documents.Troop;
import br.com.clashapi.repositorys.TroopRepository;

@Service
public class TroopServiceImpl {

	  @Autowired
	    private TroopRepository troopRepository;

	  public List<String> getCardNamesByIds(List<Integer> cardIds) {
	        // Busca na coleção 'troop' por IDs dentro de 'team.card1.id'
	        List<Troop> troops = troopRepository.findAll();

	        // Extrai os nomes das cartas de 'team.card1'
	        return troops.stream()
	                     .map(troop -> troop.getTeam().getCard1().getName())
	                     .collect(Collectors.toList());
	    }
}
