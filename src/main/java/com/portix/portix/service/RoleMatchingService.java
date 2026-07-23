package com.portix.portix.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portix.portix.model.RoleMatch;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMatchingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<RoleMatch> matchRoles(List<String> candidateSkills) {

        List<RoleMatch> matches = new ArrayList<>();

        try {

        	InputStream inputStream =
        	        new ClassPathResource("roles.json").getInputStream();

        	JsonNode root =
        	        objectMapper.readTree(inputStream);

        	root.fieldNames().forEachRemaining(roleName -> {

        	    JsonNode role = root.get(roleName);

        	    JsonNode skillsNode = role.get("skills");

        	    JsonNode roadmapNode = role.get("learningRoadmap");

        	    String category =
        	            role.get("category").asText();

        	    List<String> matchedSkills = new ArrayList<>();

        	    List<String> missingSkills = new ArrayList<>();

        	    int matched = 0;

        	    int total = skillsNode.size();

        	    for (JsonNode skill : skillsNode) {

        	        String requiredSkill =
        	                skill.asText();

        	        boolean found = false;

        	        for (String candidateSkill : candidateSkills) {

        	            if (candidateSkill.equalsIgnoreCase(requiredSkill)) {

        	                matched++;

        	                matchedSkills.add(requiredSkill);

        	                found = true;

        	                break;

        	            }

        	        }

        	        if (!found) {

        	            missingSkills.add(requiredSkill);

        	        }

        	    }

        	    int score = total == 0
        	            ? 0
        	            : (matched * 100) / total;

        	    RoleMatch match = new RoleMatch();

        	    match.setRoleName(roleName);

        	    match.setCategory(category);

        	    match.setMatchPercentage(score);

        	    match.setMatchedSkills(matchedSkills);

        	    match.setMissingSkills(missingSkills);

        	    List<String> roadmap = new ArrayList<>();

        	    for (JsonNode node : roadmapNode) {

        	        roadmap.add(node.asText());

        	    }

        	    match.setLearningRoadmap(roadmap);

        	    matches.add(match);

        	});

        	matches.sort((a, b) ->
        	        Integer.compare(
        	                b.getMatchPercentage(),
        	                a.getMatchPercentage()
        	        )
        	);
        } catch (Exception e) {

            e.printStackTrace();

        }
        return matches;

        }
        }

 