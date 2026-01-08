package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublicationType / 출판 유형
 *
 * DTD: <!ELEMENT PublicationType (#PCDATA)>
 * DTD: <!ATTLIST PublicationType UI CDATA #REQUIRED>
 *
 * KR: 논문 출판 유형 (예: "Journal Article", "Review")
 * EN: Article publication type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationType {

    /**
     * UI (고유 식별자, 필수) / UI (unique identifier, required)
     */
    private String ui;

    /**
     * 출판 유형 / Publication type
     */
    private String value;
}
