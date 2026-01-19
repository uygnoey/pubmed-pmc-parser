package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Section / 섹션
 *
 * DTD: <!ELEMENT Section (LocationLabel?, SectionTitle?, Section*)>
 *
 * KR: 도서 섹션 (재귀 구조)
 * EN: Book section (recursive structure)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    /**
     * 위치 레이블 / Location label
     */
    private LocationLabel locationLabel;

    /**
     * 섹션 제목 / Section title
     */
    private SectionTitle sectionTitle;

    /**
     * 하위 섹션 목록 (재귀) / Sub-section list (recursive)
     */
    private List<Section> sections;
}
