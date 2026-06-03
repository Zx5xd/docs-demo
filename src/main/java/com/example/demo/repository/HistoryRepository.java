package com.example.demo.repository;

import com.example.demo.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Query("SELECT h FROM History h WHERE h.history_doc_id = :docId ORDER BY h.history_version DESC")
    List<History> findByHistory_doc_idOrderByHistory_versionDesc(@Param("docId") int docId);

    @Query("SELECT h FROM History h WHERE h.history_doc_id = :docId AND h.history_version = :version")
    Optional<History> findByHistory_doc_idAndHistory_version(@Param("docId") int docId, @Param("version") int version);
}
