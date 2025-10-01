package com.fiteasy.repository;

import com.fiteasy.model.HelpingTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface HelpingToolRepository extends JpaRepository<HelpingTool, Long> {

    // Helping tools functionality
    List<HelpingTool> findByUserId(Long userId);
    List<HelpingTool> findByUserIdAndType(Long userId, HelpingTool.ToolType type);

    // Dashboard counts
    long countByUserId(Long userId);
    long countByUserIdAndType(Long userId, HelpingTool.ToolType type);

    // Search functionality
    List<HelpingTool> findByUrlContainingIgnoreCase(String url);
    List<HelpingTool> findByDescriptionContainingIgnoreCase(String description);
    List<HelpingTool> findByType(HelpingTool.ToolType type);

    // Delete operations
    @Modifying
    @Transactional
    @Query("DELETE FROM HelpingTool h WHERE h.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM HelpingTool h WHERE h.userId = :userId AND h.type = :type")
    void deleteByUserIdAndType(@Param("userId") Long userId, @Param("type") HelpingTool.ToolType type);

    // Custom queries
    @Query("SELECT h FROM HelpingTool h WHERE h.userId = :userId ORDER BY h.type, h.createdAt DESC")
    List<HelpingTool> findByUserIdOrderByTypeAndCreatedAt(@Param("userId") Long userId);

    @Query("SELECT h FROM HelpingTool h WHERE h.url LIKE %:keyword% OR h.description LIKE %:keyword%")
    List<HelpingTool> searchByKeyword(@Param("keyword") String keyword);
}