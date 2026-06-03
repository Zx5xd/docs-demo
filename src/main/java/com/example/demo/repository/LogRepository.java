package com.example.demo.repository;

import com.example.demo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {

    @Query("SELECT l FROM Log l WHERE l.docs.doc_id = :docId ORDER BY l.log_timestamp DESC")
    List<Log> findByDocs_Doc_idOrderByLog_timestampDesc(@Param("docId") int docId);
}
