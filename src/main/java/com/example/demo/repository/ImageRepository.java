package com.example.demo.repository;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i FROM Image i WHERE i.docs.doc_id = :docId")
    List<Image> findByDocs_Doc_id(@Param("docId") int docId);
}
