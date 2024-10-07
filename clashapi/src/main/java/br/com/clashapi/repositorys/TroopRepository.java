package br.com.clashapi.repositorys;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.clashapi.documents.Troop;

@Repository
public interface TroopRepository extends MongoRepository<Troop, String>{
	 List<Troop> findByTeamCard1IdIn(List<Integer> cardIds);
	 
	 @Query("{'team.card1.id': { $in: ?0 }}")
	    List<Troop> findByCardIds(List<Integer> cardIds);

}
