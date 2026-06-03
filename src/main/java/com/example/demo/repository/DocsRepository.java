package com.example.demo.repository;

import com.example.demo.entity.Docs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocsRepository extends JpaRepository<Docs, Integer> {

    @EntityGraph(attributePaths = {"category", "writer"})
    @Query("SELECT d FROM Docs d WHERE d.doc_id = :docId")
    Optional<Docs> findWithDetailsByDoc_id(@Param("docId") int docId);

    @EntityGraph(attributePaths = {"category", "writer"})
    @Query("SELECT d FROM Docs d WHERE d.category.category_id = :categoryId AND d.doc_weight > :docWeight ORDER BY d.doc_weight ASC")
    List<Docs> findNextByCategoryIdAndDocWeightGreaterThan(
            @Param("categoryId") int categoryId,
            @Param("docWeight") int docWeight,
            Pageable pageable);

    @EntityGraph(attributePaths = {"category", "writer"})
    @Query("SELECT d FROM Docs d WHERE d.category.category_id = :categoryId ORDER BY d.doc_weight ASC")
    List<Docs> findByCategoryIdOrderByDocWeightAsc(@Param("categoryId") int categoryId);
}
