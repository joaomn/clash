package br.com.clashapi.documents;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
	 private String tag;     
	    private Clan clan;     

	    private Card card1;
	    private Card card2;
	    private Card card3;
	    private Card card4;
	    private Card card5;
	    private Card card6;
	    private Card card7;
	    private Card card8;

	    
	    private Cards cards;
	    
	    
	    private int startingTrophies; 
	    private int crowns;
	    private TotalCard totalcard; 
	    private Troop troop; 
	    private Structure structure;
}
