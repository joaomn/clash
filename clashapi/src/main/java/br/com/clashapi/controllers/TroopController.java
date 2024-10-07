package br.com.clashapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clashapi.service.implement.TroopServiceImpl;

@RestController
@RequestMapping("/api/troops")
@CrossOrigin("*")
public class TroopController {

    @Autowired
    private TroopServiceImpl troopService;

    @PostMapping("/card-names")
    public ResponseEntity<List<String>> getCardNames(@RequestBody List<Integer> cardIds) {
        List<String> cardNames = troopService.getCardNamesByIds(cardIds);
        return new ResponseEntity<>(cardNames, HttpStatus.OK);
    }
}
