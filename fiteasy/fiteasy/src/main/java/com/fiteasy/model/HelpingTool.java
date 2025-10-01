package com.fiteasy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "helping_tools")
public class HelpingTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToolType type;

    @Column(nullable = false, length = 1000)
    private String url;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum ToolType {
        youtube, equipment
    }

    // Constructors
    public HelpingTool() {
        this.createdAt = LocalDateTime.now();
    }

    public HelpingTool(Long userId, ToolType type, String url) {
        this();
        this.userId = userId;
        this.type = type;
        this.url = url;
    }

    public HelpingTool(Long userId, ToolType type, String url, String description) {
        this();
        this.userId = userId;
        this.type = type;
        this.url = url;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public ToolType getType() { return type; }
    public void setType(ToolType type) { this.type = type; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "HelpingTool{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}