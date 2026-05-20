package com.example.demo.mapper;

import com.example.demo.dto.LogDTO;
import com.example.demo.entity.Docs;
import com.example.demo.entity.Log;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogMapper {

    private final UserMapper userMapper;
    private final DocsMapper docsMapper;

    public LogMapper(UserMapper userMapper, DocsMapper docsMapper) {
        this.userMapper = userMapper;
        this.docsMapper = docsMapper;
    }

    public LogDTO toDto(Log log) {
        if (log == null) {
            return null;
        }
        LogDTO dto = new LogDTO();
        dto.setLog_id(log.getLog_id());
        if (log.getAuthor() != null) {
            dto.setLog_author(log.getAuthor().getUser_login_id());
        }
        if (log.getDocs() != null) {
            dto.setLog_doc_id(log.getDocs().getDoc_id());
        }
        dto.setLog_doc_name(log.getLog_doc_name());
        dto.setLog_content(log.getLog_content());
        dto.setLog_action_type(log.getLog_action_type());
        dto.setLog_timestamp(log.getLog_timestamp());
        return dto;
    }

    public Log toEntity(LogDTO dto, User author, Docs docs) {
        if (dto == null) {
            return null;
        }
        Log log = new Log();
        log.setLog_id(dto.getLog_id());
        log.setAuthor(author);
        log.setDocs(docs);
        log.setLog_doc_name(dto.getLog_doc_name());
        log.setLog_content(dto.getLog_content());
        log.setLog_action_type(dto.getLog_action_type());
        log.setLog_timestamp(dto.getLog_timestamp());
        return log;
    }

    public Log toEntity(LogDTO dto) {
        if (dto == null) {
            return null;
        }
        User author = dto.getLog_author() != null
                ? userMapper.userRefByLoginId(dto.getLog_author())
                : null;
        Docs docs = docsMapper.docsRef(dto.getLog_doc_id());
        return toEntity(dto, author, docs);
    }

    public void updateEntity(Log log, LogDTO dto, User author, Docs docs) {
        if (log == null || dto == null) {
            return;
        }
        if (author != null) {
            log.setAuthor(author);
        }
        if (docs != null) {
            log.setDocs(docs);
        }
        log.setLog_doc_name(dto.getLog_doc_name());
        log.setLog_content(dto.getLog_content());
        log.setLog_action_type(dto.getLog_action_type());
        log.setLog_timestamp(dto.getLog_timestamp());
    }

    public List<LogDTO> toDtoList(List<Log> logs) {
        if (logs == null) {
            return List.of();
        }
        return logs.stream().map(this::toDto).toList();
    }

    public List<Log> toEntityList(List<LogDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
