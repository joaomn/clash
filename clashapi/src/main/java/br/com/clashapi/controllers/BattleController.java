package br.com.clashapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.clashapi.documents.DTOs.ComboRequestDTO;
import br.com.clashapi.documents.DTOs.DeckDTO;
import br.com.clashapi.documents.DTOs.SpecialConditionWinsRequestDTO;
import br.com.clashapi.documents.DTOs.WinAndLossResponseRecod;
import br.com.clashapi.documents.DTOs.WinningCombosRequestDTO;
import br.com.clashapi.service.implement.BattleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/battles")
public class BattleController {

	@Autowired
	private BattleServiceImpl battleService;

	@Operation(description = "Calcule a porcentagem de vitórias e derrotas utilizando a carta X(parâmetro) ocorridas em um intervalo de timestamps (parâmetro).")
	@GetMapping("/win-loss-percentage")
	public ResponseEntity<?> getWinLossPercentageForCard(@RequestParam int cardId, @RequestParam String startTime,
			@RequestParam String endTime) {
		try {
			WinAndLossResponseRecod percentage = battleService.calculateWinLossPercentageForCard(cardId, startTime,
					endTime);
			return ResponseEntity.status(HttpStatus.OK).body(percentage);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Liste os decks completos que produziram mais de X% (parâmetro) de vitórias ocorridas em um intervalo de timestamps (parâmetro).")
	@PostMapping("/winning-decks")
	public ResponseEntity<List<List<Integer>>> getDecksWithMoreThanXPercentWins(@RequestBody DeckDTO dto) {
		try {
			List<List<Integer>> decks = battleService.getDecksWithMoreThanXPercentWins(dto.getPercentage(),
					dto.getStartTime(), dto.getEndTime());

			return new ResponseEntity<>(decks, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Erro ao buscar decks: " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Calcule a quantidade de derrotas utilizando o combo de cartas\r\n"
			+ "(X1,X2, ...) (parâmetro) ocorridas em um intervalo de timestamps\r\n"
			+ "(parâmetro).")
	@PostMapping("/losses-for-combo")
	public ResponseEntity<Long> getLossesForCombo(@RequestBody ComboRequestDTO comboRequest) {
		try {
			long losses = battleService.calculateLossesForCombo(comboRequest.getCardIds(), comboRequest.getStartTime(),
					comboRequest.getEndTime());
			return new ResponseEntity<>(losses, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Calcule a quantidade de vitórias envolvendo a carta X (parâmetro) nos\r\n"
			+ "casos em que o vencedor possui Z% (parâmetro) menos troféus do que\r\n"
			+ "o perdedor, a partida durou menos de 2 minutos, e o perdedor derrubou ao menos duas torres do adversário.")
	@PostMapping("/special-condition-wins")
	public ResponseEntity<Long> getWinsWithSpecialConditions(@RequestBody SpecialConditionWinsRequestDTO requestDTO) {
		try {
			// Chamar o serviço e calcular as vitórias com as condições especiais
			long wins = battleService.calculateWinsWithSpecialConditions(requestDTO.getCardId(),
					requestDTO.getPercentageLessTrophies(), requestDTO.getStartTime(), requestDTO.getEndTime());

			// Retornar a contagem de vitórias
			return new ResponseEntity<>(wins, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Liste o combo de cartas (eg: carta 1, carta 2, carta 3... carta n) de\r\n"
			+ "tamanho N (parâmetro) que produziram mais de Y% (parâmetro) de vitórias ocorridas em um intervalo de timestamps (parâmetro).")
	@PostMapping("/winning-combos")
	public ResponseEntity<List<List<Integer>>> getWinningCombos(
			@RequestBody WinningCombosRequestDTO winningCombosRequest) {
		try {
			List<List<Integer>> combos = battleService.calculateWinningCombosWithPercentage(
					winningCombosRequest.getSize(), winningCombosRequest.getPercentage(),
					winningCombosRequest.getStartTime(), winningCombosRequest.getEndTime());

			return new ResponseEntity<>(combos, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Erro ao buscar combos vencedores: " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Obter a tag do maior vencedor")
	@GetMapping("/highest-player-tag")
	public ResponseEntity<String> getPlayerWithHighestTotalCardLevel() {
		try {
			String playerName = battleService.getPlayerWithHighestTotalCardLevel();
			return new ResponseEntity<>(playerName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Erro ao buscar o jogador.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(description = "Obter a tag do clan com maior numero de vitorias")
	@GetMapping("/highest-clan-tag")
	public ResponseEntity<String> getClanWithHighestTotalCardLevel() {
		try {
			String playerName = battleService.getClanWithHighestTotalCardLevel();
			return new ResponseEntity<>(playerName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Erro ao buscar o clan.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(description = "Obter o numero de vitorias e derrotas de um player pela sua tag")
	@GetMapping("/player-win-loss")
	public ResponseEntity<WinAndLossResponseRecod> getPlayerWinLoss(@RequestParam String playerTag,
			@RequestParam String startTime, @RequestParam String endTime) {

		try {
			// Chama o serviço para obter o registro de vitórias e derrotas
			WinAndLossResponseRecod record = battleService.getPlayerWinLossRecord(playerTag, startTime, endTime);
			return new ResponseEntity<>(record, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}