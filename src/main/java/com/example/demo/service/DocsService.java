package com.example.demo.service;

import com.example.demo.dto.DocsDTO;
import com.example.demo.entity.Docs;
import com.example.demo.exception.DocNotFoundException;
import com.example.demo.mapper.DocsMapper;
import com.example.demo.repository.DocsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DocsService {

    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    public DocsService(DocsRepository docsRepository, DocsMapper docsMapper) {
        this.docsRepository = docsRepository;
        this.docsMapper = docsMapper;
    }

    public DocsDTO findById(int docId) {
        Docs docs = docsRepository.findWithDetailsByDoc_id(docId)
                .orElseThrow(() -> new DocNotFoundException(docId));
        return docsMapper.toDto(docs);
    }

    public List<DocsDTO> findByCategoryId(int categoryId) {
        return docsMapper.toDtoList(docsRepository.findByCategory_Category_idOrderByDoc_weightAsc(categoryId));
    }

    /**
     * 같은 카테고리 안에서 현재 문서보다 doc_weight가 큰 문서 중 가장 작은 weight를 가진 문서(다음 문서).
     */
    public Optional<DocsDTO> findNextByWeight(int docId) {
        Docs current = docsRepository.findWithDetailsByDoc_id(docId)
                .orElseThrow(() -> new DocNotFoundException(docId));

        int categoryId = current.getCategory().getCategory_id();
        int currentWeight = current.getDoc_weight();

        return docsRepository
                .findFirstByCategory_Category_idAndDoc_weightGreaterThanOrderByDoc_weightAsc(
                        categoryId, currentWeight)
                .map(docsMapper::toDto);
    }
}
