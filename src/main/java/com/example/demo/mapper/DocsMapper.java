package com.example.demo.mapper;

import com.example.demo.dto.DocsDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Docs;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocsMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public DocsMapper(UserMapper userMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    public DocsDTO toDto(Docs docs) {
        if (docs == null) {
            return null;
        }
        DocsDTO dto = new DocsDTO();
        dto.setDoc_id(docs.getDoc_id());
        dto.setDoc_title(docs.getDoc_title());
        dto.setDoc_content(docs.getDoc_content());
        dto.setDoc_createdAt(docs.getDoc_createdAt());
        dto.setDoc_updatedAt(docs.getDoc_updatedAt());
        if (docs.getWriter() != null) {
            dto.setDoc_writer(docs.getWriter().getUser_login_id());
        }
        if (docs.getCategory() != null) {
            dto.setDoc_category(docs.getCategory().getCategory_id());
        }
        dto.setDoc_weight(docs.getDoc_weight());
        dto.setDoc_type(docs.getDoc_type());
        return dto;
    }

    public Docs toEntity(DocsDTO dto, User writer, Category category) {
        if (dto == null) {
            return null;
        }
        Docs docs = new Docs();
        docs.setDoc_id(dto.getDoc_id());
        docs.setDoc_title(dto.getDoc_title());
        docs.setDoc_content(dto.getDoc_content());
        docs.setDoc_createdAt(dto.getDoc_createdAt());
        docs.setDoc_updatedAt(dto.getDoc_updatedAt());
        docs.setWriter(writer);
        docs.setCategory(category);
        docs.setDoc_weight(dto.getDoc_weight());
        docs.setDoc_type(dto.getDoc_type());
        return docs;
    }

    public Docs toEntity(DocsDTO dto) {
        if (dto == null) {
            return null;
        }
        User writer = dto.getDoc_writer() != null
                ? userMapper.userRefByLoginId(dto.getDoc_writer())
                : null;
        Category category = categoryMapper.categoryRef(dto.getDoc_category());
        return toEntity(dto, writer, category);
    }

    public void updateEntity(Docs docs, DocsDTO dto, User writer, Category category) {
        if (docs == null || dto == null) {
            return;
        }
        docs.setDoc_title(dto.getDoc_title());
        docs.setDoc_content(dto.getDoc_content());
        docs.setDoc_createdAt(dto.getDoc_createdAt());
        docs.setDoc_updatedAt(dto.getDoc_updatedAt());
        if (writer != null) {
            docs.setWriter(writer);
        }
        if (category != null) {
            docs.setCategory(category);
        }
        docs.setDoc_weight(dto.getDoc_weight());
        docs.setDoc_type(dto.getDoc_type());
    }

    public Docs docsRef(int docId) {
        Docs docs = new Docs();
        docs.setDoc_id(docId);
        return docs;
    }

    public List<DocsDTO> toDtoList(List<Docs> docsList) {
        if (docsList == null) {
            return List.of();
        }
        return docsList.stream().map(this::toDto).toList();
    }

    public List<Docs> toEntityList(List<DocsDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
