package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Body / 본문
 *
 * DTD: <!ELEMENT body (%body-model;)*>
 *
 * KR: 논문의 본문 내용
 * EN: Article body content
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Body {

    /**
     * 섹션 목록 / Section list
     */
    private List<Sec> sections;

    /**
     * 문단 목록 / Paragraph list
     */
    private List<P> paragraphs;

    /**
     * 원시 내용 / Raw content
     */
    private String content;
}
