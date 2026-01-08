package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AffAlternatives / 소속 대체
 *
 * DTD: <!ELEMENT aff-alternatives (aff+)>
 *
 * KR: 다양한 언어 또는 형식의 소속 정보
 * EN: Affiliation information in various languages or formats
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffAlternatives {

    /**
     * 소속 목록 (1개 이상 필수) / Affiliation list (at least one required)
     */
    private List<Aff> affiliations;
}
