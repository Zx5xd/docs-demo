package com.example.demo.mapper;

import com.example.demo.dto.ImageDTO;
import com.example.demo.entity.Docs;
import com.example.demo.entity.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMapper {

    private final DocsMapper docsMapper;

    public ImageMapper(DocsMapper docsMapper) {
        this.docsMapper = docsMapper;
    }

    public ImageDTO toDto(Image image) {
        if (image == null) {
            return null;
        }
        ImageDTO dto = new ImageDTO();
        dto.setImg_id(image.getImg_id());
        if (image.getDocs() != null) {
            dto.setDoc_id(image.getDocs().getDoc_id());
        }
        dto.setImg_name(image.getImg_name());
        dto.setImg_alt_text(image.getImg_alt_text());
        dto.setImg_content_type(image.getImg_content_type());
        dto.setImg_data(image.getImg_data());
        dto.setImg_createdAt(image.getImg_createdAt());
        return dto;
    }

    public ImageDTO toDtoWithoutData(Image image) {
        ImageDTO dto = toDto(image);
        if (dto != null) {
            dto.setImg_data(null);
        }
        return dto;
    }

    public Image toEntity(ImageDTO dto, Docs docs) {
        if (dto == null) {
            return null;
        }
        Image image = new Image();
        image.setImg_id(dto.getImg_id());
        image.setDocs(docs);
        image.setImg_name(dto.getImg_name());
        image.setImg_alt_text(dto.getImg_alt_text());
        image.setImg_content_type(dto.getImg_content_type());
        image.setImg_data(dto.getImg_data());
        image.setImg_createdAt(dto.getImg_createdAt());
        return image;
    }

    public Image toEntity(ImageDTO dto) {
        if (dto == null) {
            return null;
        }
        Docs docs = docsMapper.docsRef(dto.getDoc_id());
        return toEntity(dto, docs);
    }

    public void updateEntity(Image image, ImageDTO dto, Docs docs) {
        if (image == null || dto == null) {
            return;
        }
        if (docs != null) {
            image.setDocs(docs);
        }
        image.setImg_name(dto.getImg_name());
        image.setImg_alt_text(dto.getImg_alt_text());
        image.setImg_content_type(dto.getImg_content_type());
        if (dto.getImg_data() != null) {
            image.setImg_data(dto.getImg_data());
        }
        image.setImg_createdAt(dto.getImg_createdAt());
    }

    public List<ImageDTO> toDtoList(List<Image> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream().map(this::toDto).toList();
    }

    public List<ImageDTO> toDtoListWithoutData(List<Image> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream().map(this::toDtoWithoutData).toList();
    }

    public List<Image> toEntityList(List<ImageDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
