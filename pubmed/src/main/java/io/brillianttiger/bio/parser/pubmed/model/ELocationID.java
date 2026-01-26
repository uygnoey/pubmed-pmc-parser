package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ELocationID (Electronic Location Identifier) / 전자 위치 식별자
 *
 * DTD: <!ELEMENT ELocationID (#PCDATA)>
 * DTD: <!ATTLIST ELocationID
 *          EIdType (doi | pii) #REQUIRED
 *          ValidYN (Y | N) "Y">
 *
 * KR: 전자 출판물의 위치 식별자 (DOI, PII 등)
 * EN: Electronic publication location identifier (DOI, PII, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ELocationID {

    /**
     * 식별자 유형: doi | pii (필수) / Identifier type (required)
     *
     * DTD: EIdType (doi | pii) #REQUIRED
     */
    private EIdType eIdType;

    /**
     * 유효 여부: Y | N (기본값: "Y") / Valid flag (default: "Y")
     */
    @Builder.Default
    private String validYN = "Y";

    /**
     * 식별자 값 / Identifier value
     */
    private String value;
}
