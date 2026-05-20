package com.example.demo.exception;

public class DocNotFoundException extends RuntimeException {

    public DocNotFoundException(int docId) {
        super("문서를 찾을 수 없습니다. doc_id=" + docId);
    }
}
