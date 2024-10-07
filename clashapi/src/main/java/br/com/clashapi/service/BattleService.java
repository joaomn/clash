package br.com.clashapi.service;

import java.util.List;

import br.com.clashapi.documents.Battle;
import br.com.clashapi.documents.DTOs.WinAndLossResponseRecod;

public interface BattleService {
	
	WinAndLossResponseRecod calculateWinLossPercentageForCard(int cardId, String startTime, String endTime);
	List<List<Integer>> getDecksWithMoreThanXPercentWins(int percentage, String startTime, String endTime);
    long calculateLossesForCombo(List<Integer> cardIds, String startTime, String endTime);
    long calculateWinsWithSpecialConditions(int cardId, int percentageLessTrophies, String startTime, String endTime);
    List<List<Integer>> calculateWinningCombosWithPercentage(int comboSize, double minPercentage, String startTime, String endTime);

}
