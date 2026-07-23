package com.portix.portix.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.portix.portix.model.RoleMatch;
import org.springframework.stereotype.Service;
import com.portix.portix.service.RoleMatchingService;
import com.portix.portix.model.ResumeAnalysis;

/**
 * ===========================================================
 * PORTIX - Resume Analysis Service
 * ===========================================================
 *
 * Responsibilities
 * ----------------
 * 1. Analyze resume skills
 * 2. Calculate ATS score
 * 3. Identify matched skills
 * 4. Identify missing skills
 * 5. Generate improvement suggestions
 *
 * Future Responsibilities
 * -----------------------
 * - Job specific ATS analysis
 * - AI powered suggestions
 * - Keyword weighting
 * - Industry specific scoring
 * - Resume ranking
 *
 * ===========================================================
 */

@Service
public class ResumeAnalysisService {

    /**
     * Master skill list.
     
     * Later this list will come from
     * the database according to the
     * selected job role.
     */

	private final RoleMatchingService roleMatchingService;

	public ResumeAnalysisService(RoleMatchingService roleMatchingService) {
	    this.roleMatchingService = roleMatchingService;
	}

    /**
     * Performs ATS analysis.
     *
     * @param resumeText Resume text
     * @return ResumeAnalysis
     */

    public ResumeAnalysis analyze(String resumeText) {

        if (resumeText == null) {
            resumeText = "";
        }

        resumeText = resumeText.toLowerCase();

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // ================= MATCH SKILLS =================

        List<String> candidateSkills = new ArrayList<>();

        for (String word : resumeText.split("[\\s,\\n]+")) {

            word = word.trim();

            if (!word.isEmpty()) {
                candidateSkills.add(word);
            }

        }

        List<RoleMatch> matches =
                roleMatchingService.matchRoles(candidateSkills);

        RoleMatch bestMatch = matches.get(0);

        matchedSkills = bestMatch.getMatchedSkills();

        missingSkills = bestMatch.getMissingSkills();

        // ================= SCORE =================

        int score = bestMatch.getMatchPercentage();
        // ================= SUGGESTIONS =================

        if (score < 60) {

            suggestions.add("Add more technical skills.");
            suggestions.add("Build more Java Spring Boot projects.");
            suggestions.add("Mention internships or real-world experience.");

        }

        for (String skill : missingSkills) {

            String s = skill.toLowerCase().trim();

            switch (s) {

                case "github":
                    suggestions.add("Add your GitHub profile.");
                    break;

                case "rest api":
                    suggestions.add("Mention REST API experience.");
                    break;

                case "hibernate":
                    suggestions.add("Learn Hibernate/JPA.");
                    break;

                case "fastapi":
                    suggestions.add("Learn FastAPI framework.");
                    break;

                default:
                    suggestions.add("Consider adding " + skill + " to your resume.");
                    break;
            }
        }

        // ================= BUILD RESPONSE =================

        ResumeAnalysis analysis = new ResumeAnalysis();

        analysis.setResumeScore(score);
        analysis.setMatchedSkills(matchedSkills);
        analysis.setMissingSkills(missingSkills);
        analysis.setSuggestions(suggestions);

        analysis.setMatchedSkillCount(matchedSkills.size());
        analysis.setTotalSkillCount(
                matchedSkills.size() + missingSkills.size()
        );
        
        
        // ================= RESUME STRENGTH =================

        if (score >= 85) {

            analysis.setResumeStrength("Excellent");
            analysis.setRecommendedRole(bestMatch.getRoleName());

        } else if (score >= 70) {

            analysis.setResumeStrength("Good");
            analysis.setRecommendedRole(bestMatch.getRoleName());

        } else if (score >= 50) {

            analysis.setResumeStrength("Average");
            analysis.setRecommendedRole(bestMatch.getRoleName());

        } else {

            analysis.setResumeStrength("Needs Improvement");
            analysis.setRecommendedRole(bestMatch.getRoleName());

        }

        return analysis;
    }
    

}
