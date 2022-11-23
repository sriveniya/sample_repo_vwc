package com.sample.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.exception.ResourceNotFoundException;
import com.sample.model.Team;
import com.sample.repository.TeamRepository;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TeamController {
	 @Autowired
	  TeamRepository tr;

	  @GetMapping("/tutorials")
	  public ResponseEntity<List<Team>> getAllTeam(@RequestParam(required = false) String name) {
	    List<Team> teams = new ArrayList<Team>();

	    if (name == null)
	      tr.findAll().forEach(teams::add);
	    else
	      tr.findByNameContaining(name).forEach(teams::add);

	    if (teams.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }

	    return new ResponseEntity<>(teams, HttpStatus.OK);
	  }

	  @GetMapping("/tutorials/{id}")
	  public ResponseEntity<Team> getTeamById(@PathVariable("id") long id) throws ResourceNotFoundException {
	    Team tutorial = tr.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

	    return new ResponseEntity<>(tutorial, HttpStatus.OK);
	  }

	  @PostMapping("/tutorials")
	  public ResponseEntity<Team> createTeam(@RequestBody Team team) {
	    Team _tutorial = tr.save(new Team(team.getName(), team.getLocation(), true));
	    return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
	  }

	  @PutMapping("/tutorials/{id}")
	  public ResponseEntity<Team> updateTeam(@PathVariable("id") long id, @RequestBody Team team) throws ResourceNotFoundException {
	    Team _tutorial = tr.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

	    _tutorial.setName(team.getName());
	    _tutorial.setLocation(team.getLocation());
	    _tutorial.setStatus(team.isStatus());
	    
	    return new ResponseEntity<>(tr.save(_tutorial), HttpStatus.OK);
	  }

	  @DeleteMapping("/tutorials/{id}")
	  public ResponseEntity<HttpStatus> deleteTeam(@PathVariable("id") long id) {
	    tr.deleteById(id);
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }

	  @DeleteMapping("/tutorials")
	  public ResponseEntity<HttpStatus> deleteAllTeam() {
	    tr.deleteAll();
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }

	  @GetMapping("/tutorials/published")
	  public ResponseEntity<List<Team>> findBystatus() {
	    List<Team> tutorials = tr.findBystatus(true);

	    if (tutorials.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    
	    return new ResponseEntity<>(tutorials, HttpStatus.OK);
	  }
	}



