package com.example.demo.mapper;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setCategory_id(category.getCategory_id());
        dto.setCategory_name(category.getCategory_name());
        dto.setCategory_createdAt(category.getCategory_createdAt());
        dto.setCategory_updatedAt(category.getCategory_updatedAt());
        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setCategory_id(dto.getCategory_id());
        category.setCategory_name(dto.getCategory_name());
        category.setCategory_createdAt(dto.getCategory_createdAt());
        category.setCategory_updatedAt(dto.getCategory_updatedAt());
        return category;
    }

    public void updateEntity(Category category, CategoryDTO dto) {
        if (category == null || dto == null) {
            return;
        }
        category.setCategory_name(dto.getCategory_name());
        category.setCategory_createdAt(dto.getCategory_createdAt());
        category.setCategory_updatedAt(dto.getCategory_updatedAt());
    }

    public List<CategoryDTO> toDtoList(List<Category> categories) {
        if (categories == null) {
            return List.of();
        }
        return categories.stream().map(this::toDto).toList();
    }

    public List<Category> toEntityList(List<CategoryDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }

    public Category categoryRef(int categoryId) {
        Category category = new Category();
        category.setCategory_id(categoryId);
        return category;
    }
}
