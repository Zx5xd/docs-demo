package com.example.demo.repository;

import com.example.demo.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> findByHistory_doc_idOrderByHistory_versionDesc(int docId);

    Optional<History> findByHistory_doc_idAndHistory_version(int docId, int version);
}
