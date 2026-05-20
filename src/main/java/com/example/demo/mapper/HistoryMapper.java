package com.example.demo.mapper;

import com.example.demo.dto.HistoryDTO;
import com.example.demo.entity.History;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryMapper {

    private final UserMapper userMapper;

    public HistoryMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public HistoryDTO toDto(History history) {
        if (history == null) {
            return null;
        }
        HistoryDTO dto = new HistoryDTO();
        dto.setHistory_id(history.getHistory_id());
        dto.setHistory_doc_id(history.getHistory_doc_id());
        dto.setHistory_version(history.getHistory_version());
        dto.setHistory_snapshot(history.getHistory_snapshot());
        dto.setHistory_action_type(history.getHistory_action_type());
        if (history.getActor() != null) {
            dto.setHistory_actor_id(history.getActor().getUser_id());
        }
        dto.setHistory_created_at(history.getHistory_created_at());
        return dto;
    }

    public History toEntity(HistoryDTO dto, User actor) {
        if (dto == null) {
            return null;
        }
        History history = new History();
        history.setHistory_id(dto.getHistory_id());
        history.setHistory_doc_id(dto.getHistory_doc_id());
        history.setHistory_version(dto.getHistory_version());
        history.setHistory_snapshot(dto.getHistory_snapshot());
        history.setHistory_action_type(dto.getHistory_action_type());
        history.setActor(actor);
        history.setHistory_created_at(dto.getHistory_created_at());
        return history;
    }

    public History toEntity(HistoryDTO dto) {
        if (dto == null) {
            return null;
        }
        User actor = userMapper.userRef(dto.getHistory_actor_id());
        return toEntity(dto, actor);
    }

    public void updateEntity(History history, HistoryDTO dto, User actor) {
        if (history == null || dto == null) {
            return;
        }
        history.setHistory_doc_id(dto.getHistory_doc_id());
        history.setHistory_version(dto.getHistory_version());
        history.setHistory_snapshot(dto.getHistory_snapshot());
        history.setHistory_action_type(dto.getHistory_action_type());
        if (actor != null) {
            history.setActor(actor);
        }
        history.setHistory_created_at(dto.getHistory_created_at());
    }

    public List<HistoryDTO> toDtoList(List<History> histories) {
        if (histories == null) {
            return List.of();
        }
        return histories.stream().map(this::toDto).toList();
    }

    public List<History> toEntityList(List<HistoryDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
