package com.fiteasy.controller;

import com.fiteasy.model.HelpingTool;
import com.fiteasy.service.HelpingToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/helping-tools")
@CrossOrigin(origins = "http://localhost:3000")
public class HelpingToolsController {

    @Autowired
    private HelpingToolService helpingToolService;


    @PostMapping
    public ResponseEntity<?> createHelpingTool(@RequestBody HelpingTool helpingTool) {
        try {
            HelpingTool createdTool = helpingToolService.createHelpingTool(helpingTool);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Helping tool added successfully");
            response.put("helpingTool", createdTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating helping tool: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HelpingTool>> getHelpingToolsByUserId(@PathVariable Long userId) {
        List<HelpingTool> tools = helpingToolService.getHelpingToolsByUserId(userId);
        return ResponseEntity.ok(tools);
    }


    @GetMapping("/user/{userId}/youtube")
    public ResponseEntity<List<HelpingTool>> getYouTubeLinks(@PathVariable Long userId) {
        List<HelpingTool> youtubeLinks = helpingToolService.getHelpingToolsByUserIdAndType(userId, HelpingTool.ToolType.youtube);
        return ResponseEntity.ok(youtubeLinks);
    }


    @GetMapping("/user/{userId}/equipment")
    public ResponseEntity<List<HelpingTool>> getEquipmentLinks(@PathVariable Long userId) {
        List<HelpingTool> equipmentLinks = helpingToolService.getHelpingToolsByUserIdAndType(userId, HelpingTool.ToolType.equipment);
        return ResponseEntity.ok(equipmentLinks);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getHelpingToolById(@PathVariable Long id) {
        Optional<HelpingTool> tool = helpingToolService.getHelpingToolById(id);
        if (tool.isPresent()) {
            return ResponseEntity.ok(tool.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Helping tool not found");
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHelpingTool(@PathVariable Long id) {
        try {
            helpingToolService.deleteHelpingTool(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Helping tool deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    @PostMapping("/youtube")
    public ResponseEntity<?> addYouTubeLink(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String url = request.get("url").toString();
            String description = request.get("description") != null ? request.get("description").toString() : "";

            // Validate YouTube URL
            if (!helpingToolService.isYouTubeUrl(url)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Please provide a valid YouTube URL");
                return ResponseEntity.badRequest().body(error);
            }

            HelpingTool youtubeTool = new HelpingTool(userId, HelpingTool.ToolType.youtube, url, description);
            HelpingTool createdTool = helpingToolService.createHelpingTool(youtubeTool);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "YouTube link added successfully");
            response.put("helpingTool", createdTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error adding YouTube link: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/equipment")
    public ResponseEntity<?> addEquipmentLink(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String url = request.get("url").toString();
            String description = request.get("description") != null ? request.get("description").toString() : "";

            // Validate URL format
            if (!helpingToolService.isValidUrl(url)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Please provide a valid URL (must start with http:// or https://)");
                return ResponseEntity.badRequest().body(error);
            }

            HelpingTool equipmentTool = new HelpingTool(userId, HelpingTool.ToolType.equipment, url, description);
            HelpingTool createdTool = helpingToolService.createHelpingTool(equipmentTool);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Equipment link added successfully");
            response.put("helpingTool", createdTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error adding equipment link: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  Bulk delete helping tools by type
    @DeleteMapping("/user/{userId}/type/{type}")
    public ResponseEntity<?> deleteHelpingToolsByType(@PathVariable Long userId, @PathVariable String type) {
        try {
            HelpingTool.ToolType toolType = HelpingTool.ToolType.valueOf(type.toLowerCase());
            helpingToolService.deleteHelpingToolsByUserIdAndType(userId, toolType);

            Map<String, String> response = new HashMap<>();
            response.put("message", "All " + type + " links deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error deleting " + type + " links: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  count of helping tools for dashboard
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Long>> getHelpingToolsCount(@PathVariable Long userId) {
        long youtubeCount = helpingToolService.getCountByUserIdAndType(userId, HelpingTool.ToolType.youtube);
        long equipmentCount = helpingToolService.getCountByUserIdAndType(userId, HelpingTool.ToolType.equipment);
        long totalCount = youtubeCount + equipmentCount;

        Map<String, Long> response = new HashMap<>();
        response.put("youtubeCount", youtubeCount);
        response.put("equipmentCount", equipmentCount);
        response.put("totalCount", totalCount);
        return ResponseEntity.ok(response);
    }

    // Search functionality
    @GetMapping("/search/url")
    public ResponseEntity<List<HelpingTool>> searchByUrl(@RequestParam String searchTerm) {
        List<HelpingTool> tools = helpingToolService.searchHelpingToolsByUrl(searchTerm);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<HelpingTool>> searchByDescription(@RequestParam String searchTerm) {
        List<HelpingTool> tools = helpingToolService.searchHelpingToolsByDescription(searchTerm);
        return ResponseEntity.ok(tools);
    }

    // Admin endpoints
    @GetMapping("/all")
    public ResponseEntity<List<HelpingTool>> getAllHelpingTools() {
        List<HelpingTool> tools = helpingToolService.getAllHelpingTools();
        return ResponseEntity.ok(tools);
    }
}