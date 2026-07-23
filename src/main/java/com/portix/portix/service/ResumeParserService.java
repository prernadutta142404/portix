package com.portix.portix.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portix.portix.model.CandidateProfile;
import com.portix.portix.model.ResumeAnalysis;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.portix.portix.entity.User;


@Service
public class ResumeParserService {
	
	

    public CandidateProfile parse(String resumeText,
                                  ResumeAnalysis analysis) {

        CandidateProfile profile = new CandidateProfile();

        if (resumeText == null || resumeText.isBlank()) {
            return profile;
        }

        // =========================
        // Basic Details
        // =========================

        profile.setName(extractName(resumeText));
        profile.setEmail(extractEmail(resumeText));
        profile.setPhone(extractPhone(resumeText));
        profile.setGithub(extractGithub(resumeText));
        profile.setLinkedin(extractLinkedIn(resumeText));

        // =========================
        // Summary
        // =========================

        profile.setSummary(
                extractSection(
                        resumeText,
                        "PROFESSIONAL SUMMARY",
                        "EDUCATION"
                )
        );

        // =========================
        // Education
        // =========================

        profile.setEducation(
                extractEducation(resumeText)
        );

        // =========================
        // Skills
        // =========================

        profile.setSkills(
                extractSkills(resumeText)
        );

        // =========================
        // Projects
        // =========================

        profile.setProjects(
                extractProjects(resumeText)
        );

        // =========================
        // Certificates
        // =========================

        profile.setCertificates(
                splitLines(
                        extractSection(
                                resumeText,
                                "CERTIFICATIONS",
                                "STRENGTHS"
                        )
                )
        );

        // =========================
        // Languages
        // =========================

        

        // =========================
        // Experience
        // =========================

        profile.setExperience(
                extractExperience(resumeText)
        );

        // =========================
        // ATS Data
        // =========================

        if (analysis != null) {

            profile.setResumeScore(
                    analysis.getResumeScore()
            );

            profile.setRecommendedRole(
                    analysis.getRecommendedRole()
            );

        }

        return profile;
    }

    // =======================================================
    // BASIC DETAILS
    // =======================================================

    private String extractName(String text) {

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {

            line = line.trim();

            if (!line.isEmpty()
                    && !line.contains("@")
                    && !line.matches(".*\\d.*")
                    && line.length() < 40) {

                return line;
            }

        }

        return "";
    }

    private String extractEmail(String text) {

        Pattern pattern = Pattern.compile(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    private String extractPhone(String text) {

        Pattern pattern = Pattern.compile(
                "(\\+91[- ]?)?[6-9]\\d{9}"
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    private String extractGithub(String text) {

        Pattern pattern = Pattern.compile(
                "(https?://)?(www\\.)?github\\.com/\\S+",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }

    private String extractLinkedIn(String text) {

        Pattern pattern = Pattern.compile(
                "(https?://)?(www\\.)?linkedin\\.com/\\S+",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group() : "";
    }
        // =======================================================
        // SKILLS
        // =======================================================

        private List<String> extractSkills(String resumeText) {

            List<String> skills = new ArrayList<>();

            try {

                InputStream inputStream =
                        new ClassPathResource("roles.json").getInputStream();

                ObjectMapper mapper = new ObjectMapper();

                JsonNode root = mapper.readTree(inputStream);

                String lowerResume = resumeText.toLowerCase();

                root.fieldNames().forEachRemaining(role -> {

                    JsonNode roleNode = root.get(role);

                    JsonNode skillArray = roleNode.get("skills");

                    if (skillArray == null) {
                        return;
                    }

                    for (JsonNode skill : skillArray) {

                        String value = skill.asText();

                        if (lowerResume.contains(value.toLowerCase())
                                && !skills.contains(value)) {

                            skills.add(value);

                        }

                    }

                });

            } catch (Exception e) {

                e.printStackTrace();

            }

            return skills;

        }

        
        
        
        
        // =======================================================
        // PROJECTS
        // =======================================================

        private List<String> extractProjects(String resumeText) {

            List<String> projects = new ArrayList<>();

            String section = extractSection(
                    resumeText,
                    "PROJECTS",
                    "CERTIFICATIONS"
            );

            if (section.isBlank()) {

                section = extractSection(
                        resumeText,
                        "PROJECTS",
                        "EDUCATION"
                );

            }

            String[] lines = section.split("\\r?\\n");

            for (int i = 0; i < lines.length; i++) {

                String line = lines[i].trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Ignore description lines
                String lower = line.toLowerCase();

                if (lower.startsWith("developed")
                        || lower.startsWith("built")
                        || lower.startsWith("implemented")
                        || lower.startsWith("created")
                        || lower.startsWith("designed")
                        || lower.startsWith("using")
                        || lower.startsWith("technology")
                        || lower.startsWith("-")
                        || lower.startsWith("•")
                        || lower.length() > 100) {

                    continue;
                }

                // A project title is usually followed by a description
                if (i + 1 < lines.length) {

                    String next = lines[i + 1].trim().toLowerCase();

                    if (next.startsWith("developed")
                            || next.startsWith("built")
                            || next.startsWith("implemented")
                            || next.startsWith("created")
                            || next.startsWith("designed")) {

                        projects.add(line);

                    }

                }

            }

            return projects;

        }
        
        
        private List<String> extractEducation(String resumeText) {

            List<String> education = new ArrayList<>();

            String section = extractSection(
                    resumeText,
                    "EDUCATION",
                    "TECHNICAL SKILLS"
            );

            if (section.isBlank()) {
                return education;
            }

            String[] lines = section.split("\\r?\\n");

            for (String line : lines) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                education.add(line);

            }

            return education;
        }
        
        
        private List<String> extractExperience(String resumeText) {

            String section = extractSection(
                    resumeText,
                    "EXPERIENCE",
                    "PROJECTS"
            );

            if (section.isBlank()) {
                return new ArrayList<>();
            }

            return splitLines(section);
        }
        
        // =======================================================
        // LANGUAGES
        // =======================================================

        
        // =======================================================
        // SECTION EXTRACTION
        // =======================================================

        private String extractSection(String text,
                                      String startHeading,
                                      String endHeading) {

            if (text == null || text.isBlank()) {
                return "";
            }

            String upperText = text.toUpperCase();

            int startIndex = upperText.indexOf(
                    startHeading.toUpperCase()
            );

            if (startIndex == -1) {
                return "";
            }

            startIndex += startHeading.length();

            int endIndex;

            if (endHeading == null || endHeading.isBlank()) {

                endIndex = text.length();

            } else {

                endIndex = upperText.indexOf(
                        endHeading.toUpperCase(),
                        startIndex
                );

                if (endIndex == -1) {
                    endIndex = text.length();
                }

            }

            return text.substring(
                    startIndex,
                    endIndex
            ).trim();

        }

        // =======================================================
        // SPLIT SECTION
        // =======================================================

        private List<String> splitLines(String section) {

            List<String> list = new ArrayList<>();

            if (section == null || section.isBlank()) {
                return list;
            }

            String[] lines = section.split("\\r?\\n");

            for (String line : lines) {

                line = line.trim();

                if (!line.isEmpty()) {
                    list.add(line);
                }

            }

            return list;

        }
    
    }
    