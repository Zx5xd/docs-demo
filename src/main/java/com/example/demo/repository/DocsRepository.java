package com.example.demo.repository;

import com.example.demo.entity.Docs;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocsRepository extends JpaRepository<Docs, Integer> {

    @EntityGraph(attributePaths = {"category", "writer"})
    Optional<Docs> findWithDetailsByDoc_id(int docId);

    @EntityGraph(attributePaths = {"category", "writer"})
    Optional<Docs> findFirstByCategory_Category_idAndDoc_weightGreaterThanOrderByDoc_weightAsc(
            int categoryId, int docWeight);

    @EntityGraph(attributePaths = {"category", "writer"})
    List<Docs> findByCategory_Category_idOrderByDoc_weightAsc(int categoryId);
}
