package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Minute / 분
 *
 * DTD: <!ELEMENT Minute (#PCDATA)>
 *
 * KR: 시각 정보 - 분
 * EN: Time information - minute
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Minute {

    /**
     * 분 / Minute
     */
    private String value;
}
