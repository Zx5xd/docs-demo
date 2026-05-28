package com.example.demo.controller;

import com.example.demo.dto.DocsDTO;
import com.example.demo.service.DocsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/docs")
public class DocsController {

    private final DocsService docsService;

    public DocsController(DocsService docsService) {
        this.docsService = docsService;
    }

    @GetMapping("/{id}")
    public DocsDTO getById(@PathVariable int id) {
        return docsService.findById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<DocsDTO> getByCategory(@PathVariable int categoryId) {
        return docsService.findByCategoryId(categoryId);
    }

    /**
     * doc_weight 기준 같은 카테고리 내 다음 문서.
     * 다음 문서가 없으면 204 No Content.
     */
    @GetMapping("/{id}/next")
    public ResponseEntity<?> getNext(@PathVariable int id) {
        return docsService.findNextByWeight(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
