package br.com.clashapi.repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.clashapi.documents.Battle;

@Repository
public interface BattleRepository extends MongoRepository<Battle, String> {
	
	 // Query 1: Calcule a porcentagem de vitórias e derrotas utilizando a carta X
    @Query(value = "{ $or: [ {'winner.cards.list': ?0}, {'loser.cards.list': ?0}], 'battleTime': { $gte: ?1, $lte: ?2 } }",
           count = true)
    long countBattlesByCardIdInTimeRange(int cardId, String startTime, String endTime);

    // Query 2: Liste os decks completos que produziram mais de X% de vitórias
    @Query(value = "{ 'winner.cards.list': { $size: 8 }, 'battleTime': { $gte: ?1, $lte: ?2 } }")
    List<?> findWinningDecksWithMoreThanXPercentWins(int percentage, String startTime, String endTime);

    // Query 3: Calcule a quantidade de derrotas utilizando o combo de cartas (X1, X2,...)
    @Query(value = "{ 'loser.cards.list': { $all: ?0 }, 'battleTime': { $gte: ?1, $lte: ?2 } }")
    long countLossesByComboInTimeRange(List<Integer> cardIds, String startTime, String endTime);

    // Query 4: Calcule a quantidade de vitórias envolvendo a carta X em condições especiais
    @Query(value = "{ 'winner.cards.list': ?0, 'winner.trophies': { $lt: ?1 }, 'loser.towersDestroyed': { $gte: 2 }, 'battleTime': { $lt: ?2 } }")
    long countWinsWithSpecialConditions(int cardId, int trophyDifference, String timeLimit);

    // Query 5: Liste o combo de cartas de tamanho N com mais de Y% de vitórias
    @Query(value = "{ 'winner.cards.list': { $size: ?0 }, 'battleTime': { $gte: ?1, $lte: ?2 } }")
    List<Battle> findWinningCombosWithSizeAndPercent(int size, int percentage, String startTime, String endTime);
    
    @Query(value = "{ 'winner.cards.list': ?0, 'battleTime': { $gte: ?1, $lte: ?2 } }", count = true)
    long countCardWins(int cardId, String startTime, String endTime);
    
    
    @Query(value = "{ 'loser.cards.list': ?0, 'battleTime': { $gte: ?1, $lte: ?2 } }", count = true)
    long countCardLosses(int cardId, String startTime, String endTime);
    
    
    @Query(value = "{ 'battleTime': { $gte: ?0, $lte: ?1 } }")
    List<Battle> findAllWinningDecksInTimeRange(String startTime, String endTime);
     
    
    List<Battle> findAllByBattleTimeBetween(String startTime, String endTime);
    
    

    @Query(value = "{}", sort = "{ 'winner.totalcard.level': -1 }")
    List<Battle> findBattleWithHighestTotalCardLevel(PageRequest pageable);
    
    @Query(value = "{ 'winner.tag': ?0, 'battleTime': { $gte: ?1, $lte: ?2 } }", count = true)
    long countWinsByTagAndTimeRange(String playerTag, String startTime, String endTime);
    
    @Query(value = "{ 'loser.tag': ?0, 'battleTime': { $gte: ?1, $lte: ?2 } }", count = true)
    long countLossesByTagAndTimeRange(String playerTag, String startTime, String endTime);

}
