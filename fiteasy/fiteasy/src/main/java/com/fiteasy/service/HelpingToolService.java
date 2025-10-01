package com.fiteasy.service;

import com.fiteasy.model.HelpingTool;
import com.fiteasy.repository.HelpingToolRepository;
import com.fiteasy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HelpingToolService {

    @Autowired
    private HelpingToolRepository helpingToolRepository;

    @Autowired
    private UserRepository userRepository;

    // PAGE 7: Create helping tool
    public HelpingTool createHelpingTool(HelpingTool helpingTool) {
        // Validate user exists
        if (!userRepository.existsById(helpingTool.getUserId())) {
            throw new RuntimeException("User not found with id: " + helpingTool.getUserId());
        }

        // Validate URL
        if (helpingTool.getUrl() == null || helpingTool.getUrl().trim().isEmpty()) {
            throw new RuntimeException("URL is required");
        }

        // Validate type
        if (helpingTool.getType() == null) {
            throw new RuntimeException("Tool type is required");
        }

        return helpingToolRepository.save(helpingTool);
    }

    // PAGE 7: Get all helping tools for user
    public List<HelpingTool> getHelpingToolsByUserId(Long userId) {
        return helpingToolRepository.findByUserId(userId);
    }

    // PAGE 7: Get helping tools by user and type
    public List<HelpingTool> getHelpingToolsByUserIdAndType(Long userId, HelpingTool.ToolType type) {
        return helpingToolRepository.findByUserIdAndType(userId, type);
    }

    // PAGE 7: Get helping tool by ID
    public Optional<HelpingTool> getHelpingToolById(Long id) {
        return helpingToolRepository.findById(id);
    }

    // PAGE 7: Update helping tool
    public HelpingTool updateHelpingTool(Long id, HelpingTool toolDetails) {
        HelpingTool tool = helpingToolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Helping tool not found with id: " + id));

        // Update fields
        if (toolDetails.getType() != null) {
            tool.setType(toolDetails.getType());
        }

        if (toolDetails.getUrl() != null && !toolDetails.getUrl().trim().isEmpty()) {
            tool.setUrl(toolDetails.getUrl());
        }

        if (toolDetails.getDescription() != null) {
            tool.setDescription(toolDetails.getDescription());
        }

        return helpingToolRepository.save(tool);
    }

    // PAGE 7: Delete helping tool
    public void deleteHelpingTool(Long id) {
        HelpingTool tool = helpingToolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Helping tool not found with id: " + id));
        helpingToolRepository.delete(tool);
    }

    // PAGE 7: Delete all helping tools by user and type
    public void deleteHelpingToolsByUserIdAndType(Long userId, HelpingTool.ToolType type) {
        List<HelpingTool> tools = helpingToolRepository.findByUserIdAndType(userId, type);
        helpingToolRepository.deleteAll(tools);
    }

    // PAGE 4: Dashboard counts
    public long getHelpingToolsCountByUserId(Long userId) {
        return helpingToolRepository.countByUserId(userId);
    }

    public long getCountByUserIdAndType(Long userId, HelpingTool.ToolType type) {
        return helpingToolRepository.countByUserIdAndType(userId, type);
    }

    // PAGE 7: Get YouTube links
    public List<HelpingTool> getYouTubeLinks(Long userId) {
        return helpingToolRepository.findByUserIdAndType(userId, HelpingTool.ToolType.youtube);
    }

    // PAGE 7: Get equipment links
    public List<HelpingTool> getEquipmentLinks(Long userId) {
        return helpingToolRepository.findByUserIdAndType(userId, HelpingTool.ToolType.equipment);
    }

    // PAGE 7: Add YouTube link specifically
    public HelpingTool addYouTubeLink(Long userId, String url, String description) {
        HelpingTool youtubeTool = new HelpingTool();
        youtubeTool.setUserId(userId);
        youtubeTool.setType(HelpingTool.ToolType.youtube);
        youtubeTool.setUrl(url);
        youtubeTool.setDescription(description);

        return createHelpingTool(youtubeTool);
    }

    // PAGE 7: Add equipment link specifically
    public HelpingTool addEquipmentLink(Long userId, String url, String description) {
        HelpingTool equipmentTool = new HelpingTool();
        equipmentTool.setUserId(userId);
        equipmentTool.setType(HelpingTool.ToolType.equipment);
        equipmentTool.setUrl(url);
        equipmentTool.setDescription(description);

        return createHelpingTool(equipmentTool);
    }

    // Delete all helping tools for user (cascade delete)
    public void deleteHelpingToolsByUserId(Long userId) {
        helpingToolRepository.deleteByUserId(userId);
    }

    // Admin functions
    public List<HelpingTool> getAllHelpingTools() {
        return helpingToolRepository.findAll();
    }

    public List<HelpingTool> searchHelpingToolsByUrl(String searchTerm) {
        return helpingToolRepository.findByUrlContainingIgnoreCase(searchTerm);
    }

    public List<HelpingTool> searchHelpingToolsByDescription(String searchTerm) {
        return helpingToolRepository.findByDescriptionContainingIgnoreCase(searchTerm);
    }

    // Utility methods
    public boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        String urlLower = url.toLowerCase();
        return urlLower.startsWith("http://") || urlLower.startsWith("https://");
    }

    public boolean isYouTubeUrl(String url) {
        if (url == null) return false;
        String urlLower = url.toLowerCase();
        return urlLower.contains("youtube.com") || urlLower.contains("youtu.be");
    }

    // BMI Calculation methods for BDD testing
    
    /**
     * Calculate BMI based on height (in cm) and weight (in kg)
     * Formula: BMI = weight / (height in meters)^2
     */
    public String calculateBMI(java.math.BigDecimal height, java.math.BigDecimal weight) {
        if (height == null || weight == null || 
            height.compareTo(java.math.BigDecimal.ZERO) <= 0 || 
            weight.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive numbers");
        }
        
        // Convert height from cm to meters
        java.math.BigDecimal heightInMeters = height.divide(new java.math.BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP);
        
        // Calculate BMI: weight / (height^2)
        java.math.BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);
        java.math.BigDecimal bmi = weight.divide(heightSquared, 2, java.math.RoundingMode.HALF_UP);
        
        return bmi.toString();
    }
    
    /**
     * Get BMI category based on BMI value
     */
    public String getBMICategory(java.math.BigDecimal bmi) {
        if (bmi.compareTo(new java.math.BigDecimal("18.5")) < 0) {
            return "Underweight";
        } else if (bmi.compareTo(new java.math.BigDecimal("25")) < 0) {
            return "Normal weight";
        } else if (bmi.compareTo(new java.math.BigDecimal("30")) < 0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
    /**
     * Get BMI message based on category
     */
    public String getBMIMessage(String category) {
        switch (category) {
            case "Underweight":
                return "Your BMI indicates you may be underweight. Consider consulting a healthcare provider.";
            case "Normal weight":
                return "Your BMI indicates a healthy weight range";
            case "Overweight":
                return "Your BMI indicates you are overweight. Consider a balanced diet and regular exercise.";
            case "Obese":
                return "Your BMI indicates obesity. Please consult a healthcare provider for guidance.";
            default:
                return "BMI calculated successfully";
        }
    }
}
