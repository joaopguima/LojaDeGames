package com.generation.lojadegames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojadegames.model.Games;
import com.generation.lojadegames.repository.GamesRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GamesController {
	
	@Autowired
	private GamesRepository gamesRepository;
	
	@GetMapping
	public ResponseEntity<List<Games>> getAll(){
		return ResponseEntity.ok(gamesRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Games> getById(@PathVariable Long id){
		return gamesRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Games>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(gamesRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping
	public ResponseEntity<Games> post (@Valid @RequestBody Games games) {
		return ResponseEntity.status(HttpStatus.CREATED).body(gamesRepository.save(games));
	}
	
	@PutMapping
	public ResponseEntity<Games>put(@Valid @RequestBody Games games){
		return gamesRepository.findById(games.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(gamesRepository.save(games)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Games> games = gamesRepository.findById(id);
		
		if(games.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		gamesRepository.deleteById(id);
	}

}
