package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Xref / 교차 참조
 *
 * DTD: <!ELEMENT xref (#PCDATA | %all-phrase;)*>
 * DTD: <!ATTLIST xref
 *          id ID #IMPLIED
 *          ref-type CDATA #IMPLIED
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * KR: 교차 참조 (문서 내부 참조)
 * EN: Cross reference (internal document reference)
 *
 * **ref-type 값**: aff, bibr, fig, table, sec, supplementary-material, fn, disp-formula
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Xref {
    /**
     * KR: ID 속성
     * EN: ID attribute
     */
    private String id;

    /**
     * KR: 참조 유형 (aff, bibr, fig, table, sec, supplementary-material, fn, disp-formula 등)
     * EN: Reference type (aff, bibr, fig, table, sec, supplementary-material, fn, disp-formula, etc.)
     */
    private String refType;

    /**
     * KR: 참조 대상 ID (공백으로 구분된 여러 ID 가능)
     * EN: Reference target ID(s) (space-separated multiple IDs possible)
     */
    private String rid;

    /**
     * KR: 특정 용도
     * EN: Specific use
     */
    private String specificUse;

    /**
     * KR: 참조 텍스트 (표시될 텍스트)
     * EN: Reference text (display text)
     */
    private String value;
}
