package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Grant / 연구비
 *
 * DTD: <!ELEMENT Grant (GrantID?, Acronym?, Agency, Country?)>
 *
 * KR: 연구비 지원 정보
 * EN: Grant funding information
 *
 * **2024년 변경**: Country가 선택적으로 변경 (기존: 필수 → 변경: 선택)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grant {

    /**
     * 연구비 ID / Grant ID
     */
    private GrantID grantID;

    /**
     * 기관 약어 / Agency acronym
     */
    private Acronym acronym;

    /**
     * 기관명 / Agency name
     */
    private Agency agency;

    /**
     * 국가 / Country
     */
    private Country country;
}
