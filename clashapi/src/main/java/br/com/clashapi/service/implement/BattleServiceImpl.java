package br.com.clashapi.service.implement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.clashapi.documents.Battle;
import br.com.clashapi.documents.Player;
import br.com.clashapi.documents.DTOs.PlayerResponseDTO;
import br.com.clashapi.documents.DTOs.WinAndLossResponseRecod;
import br.com.clashapi.repositorys.BattleRepository;
import br.com.clashapi.service.BattleService;

@Service
public class BattleServiceImpl implements BattleService {

	@Autowired
	private BattleRepository battleRepository;

	// Formato do banco de dados
	private static final DateTimeFormatter DATABASE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss+00:00");
	private static final DateTimeFormatter API_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

	private static final String API_URL = "https://api.clashroyale.com/v1/players/"; 
 static final String AUTH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjI1ODIxZDAxLWYxZWEtNDI1MS04OGY2LTRjMTE1ZGM3ZmViZCIsImlhdCI6MTcyNzk5NDU1MCwic3ViIjoiZGV2ZWxvcGVyLzEyZjliMzJjLWRjNDYtYTEzOS01NjcxLWVlOGFkODhhMmQ1OSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyIxNzkuMjUwLjI1NS4xOTgiXSwidHlwZSI6ImNsaWVudCJ9XX0.CCXvOyHeloFBLLqF_rph9Rv5n8p_VtbUj1SoKeH82-iJKbLST_YFPUcOlJbA8ZueYeup9vJQ52BUHmsnsbNa7Q"; 
																

	@Override
	public WinAndLossResponseRecod calculateWinLossPercentageForCard(int cardId, String startTime, String endTime) {
		try {
			// Converte a data de entrada para o formato do banco
			String startFormatted = convertToDatabaseFormat(startTime);
			String endFormatted = convertToDatabaseFormat(endTime);

			long totalBattles = battleRepository.countBattlesByCardIdInTimeRange(cardId, startFormatted, endFormatted);
			long wins = battleRepository.countCardWins(cardId, startFormatted, endFormatted);
			long losses = battleRepository.countCardLosses(cardId, startFormatted, endFormatted);

			double winPercentage = totalBattles > 0 ? ((double) wins / totalBattles * 100) : 0.0;
			double lossPercentage = totalBattles > 0 ? ((double) losses / totalBattles * 100) : 0.0;
			return new WinAndLossResponseRecod(winPercentage, lossPercentage);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<List<Integer>> getDecksWithMoreThanXPercentWins(int percentage, String startTime, String endTime) {
		try {
			String startFormatted = convertToDatabaseFormat(startTime);
			String endFormatted = convertToDatabaseFormat(endTime);

			List<Battle> allBattles = battleRepository.findAllWinningDecksInTimeRange(startFormatted, endFormatted);

			Map<List<Integer>, Long> deckWinsCount = new HashMap<>();
			Map<List<Integer>, Long> deckTotalCount = new HashMap<>();

			for (Battle battle : allBattles) {
				List<Integer> winningDeck = battle.getWinner().getCards().getList();
				deckWinsCount.put(winningDeck, deckWinsCount.getOrDefault(winningDeck, 0L) + 1);
				deckTotalCount.put(winningDeck, deckTotalCount.getOrDefault(winningDeck, 0L) + 1);

				List<Integer> losingDeck = battle.getLoser().getCards().getList();
				deckTotalCount.put(losingDeck, deckTotalCount.getOrDefault(losingDeck, 0L) + 1);
			}

			List<List<Integer>> filteredDecks = new ArrayList<>();

			for (Map.Entry<List<Integer>, Long> entry : deckWinsCount.entrySet()) {
				List<Integer> deck = entry.getKey();
				long wins = entry.getValue();
				long totalAppearances = deckTotalCount.getOrDefault(deck, 0L);

				double winPercentage = (double) wins / totalAppearances * 100;

				if (winPercentage > percentage) {
					filteredDecks.add(deck);
				}
			}

			return filteredDecks;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public long calculateLossesForCombo(List<Integer> cardIds, String startTime, String endTime) {
		try {
			String startFormatted = convertToDatabaseFormat(startTime);
			String endFormatted = convertToDatabaseFormat(endTime);

			List<Battle> allBattles = battleRepository.findAllWinningDecksInTimeRange(startFormatted, endFormatted);

			long losers = 0;

			for (Battle battle : allBattles) {
				List<Integer> winningDeck = battle.getLoser().getCards().getList();
				if (winningDeck.containsAll(cardIds)) {
					losers++;
				}

			}

			return losers > 0 ? losers : 0L;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public long calculateWinsWithSpecialConditions(int cardId, int percentageLessTrophies, String startTime,
			String endTime) {
		try {
			String startFormatted = convertToDatabaseFormat(startTime);
			String endFormatted = convertToDatabaseFormat(endTime);

			List<Battle> allBattles = battleRepository.findAllWinningDecksInTimeRange(startFormatted, endFormatted);

			long winCount = 0;

			for (Battle battle : allBattles) {
				Player winner = battle.getWinner();
				Player loser = battle.getLoser();

				if (winner.getCards().getList().contains(cardId)) {

					int trophyDifference = loser.getStartingTrophies() - winner.getStartingTrophies();
					double percentageDifference = (double) trophyDifference / loser.getStartingTrophies() * 100;

					if (percentageDifference >= (percentageLessTrophies / 100) && loser.getCrowns() >= 2) {
						winCount++;
					}
				}
			}

			return winCount;

		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<List<Integer>> calculateWinningCombosWithPercentage(int comboSize, double minPercentage,
			String startTime, String endTime) {
		try {
			String startFormatted = convertToDatabaseFormat(startTime);
			String endFormatted = convertToDatabaseFormat(endTime);

			List<Battle> allBattles = battleRepository.findAllWinningDecksInTimeRange(startFormatted, endFormatted);

			Map<List<Integer>, Integer> comboWinCount = new HashMap<>();
			int totalBattles = allBattles.size();

			for (Battle battle : allBattles) {
				List<Integer> winningDeck = battle.getWinner().getCards().getList();

				List<List<Integer>> combos = getCombinations(winningDeck, comboSize);

				for (List<Integer> combo : combos) {
					comboWinCount.put(combo, comboWinCount.getOrDefault(combo, 0) + 1);
				}
			}

			List<List<Integer>> winningCombos = new ArrayList<>();

			for (Map.Entry<List<Integer>, Integer> entry : comboWinCount.entrySet()) {
				List<Integer> combo = entry.getKey();
				int wins = entry.getValue();

				double winPercentage = (double) wins / totalBattles * 100;

				if (winPercentage > (minPercentage / 100)) {
					winningCombos.add(combo);
				}
			}

			return winningCombos;
		} catch (Exception e) {
			return List.of();
		}
	}

	private String convertToDatabaseFormat(String dateString) {
		LocalDateTime dateTime = LocalDateTime.parse(dateString, API_FORMATTER);
		return dateTime.format(DATABASE_FORMATTER);
	}

	private double validatePercentage(double percentage) {
		if (Double.isNaN(percentage)) {
			return 0.0;
		}
		return percentage;
	}

	public List<List<Integer>> getCombinations(List<Integer> cards, int comboSize) {
		List<List<Integer>> combinations = new ArrayList<>();
		generateCombinations(combinations, new ArrayList<>(), cards, comboSize, 0);
		return combinations;
	}

	private void generateCombinations(List<List<Integer>> combinations, List<Integer> currentCombo, List<Integer> cards,
			int comboSize, int index) {
		if (currentCombo.size() == comboSize) {
			combinations.add(new ArrayList<>(currentCombo));
			return;
		}

		for (int i = index; i < cards.size(); i++) {
			currentCombo.add(cards.get(i));
			generateCombinations(combinations, currentCombo, cards, comboSize, i + 1);
			currentCombo.remove(currentCombo.size() - 1); // Backtrack
		}
	}
	
	public String getPlayerWithHighestTotalCardLevel() {
		
		  List<Battle> battles = battleRepository.findBattleWithHighestTotalCardLevel(PageRequest.of(0, 1));

	        if (battles.isEmpty()) {
	            return "Nenhum jogador encontrado.";
	        }

	        // Pegando a primeira batalha da lista
	        Battle battle = battles.get(0);
	        Player winner = battle.getWinner();
	        String playerTag = winner.getTag();


	            return playerTag;

	       
    }
	
	public String getClanWithHighestTotalCardLevel() {
		
		  List<Battle> battles = battleRepository.findBattleWithHighestTotalCardLevel(PageRequest.of(0, 1));

	        if (battles.isEmpty()) {
	            return "Nenhum clan encontrado.";
	        }

	        // Pegando a primeira batalha da lista
	        Battle battle = battles.get(0);
	        Player winner = battle.getWinner();
	        String playerTag = winner.getClan().getTag();


	            return playerTag;

	       
  }
	
	public WinAndLossResponseRecod getPlayerWinLossRecord(String playerTag, String startTime, String endTime) {
        try {
            // Converte as datas para o formato do banco de dados
            String startFormatted = convertToDatabaseFormat(startTime);
            String endFormatted = convertToDatabaseFormat(endTime);

            // Consulta as vitórias e derrotas
            long wins = battleRepository.countWinsByTagAndTimeRange(playerTag, startFormatted, endFormatted);
            long losses = battleRepository.countLossesByTagAndTimeRange(playerTag, startFormatted, endFormatted);

            // Retorna um objeto com as contagens
            return new WinAndLossResponseRecod(wins, losses);
        } catch (Exception e) {
            e.printStackTrace();
            return new WinAndLossResponseRecod(0, 0); // Retorna 0 caso haja erro
        }
    }
}

