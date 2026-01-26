package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Note / 노트
 *
 * DTD: <!ELEMENT Note (#PCDATA)>
 *
 * KR: 추가 메모 또는 주석
 * EN: Additional note or comment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    /**
     * 노트 내용 / Note content
     */
    private String value;
}
