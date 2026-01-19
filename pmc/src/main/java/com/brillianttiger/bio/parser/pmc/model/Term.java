package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Term / 용어
 *
 * KR: 정의 목록의 용어. JATS 1.4 완전 준수 모델.
 * EN: Term in a definition list. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT term (#PCDATA | %all-phrase;)*>
 *
 *      <!ATTLIST term
 *          %jats-common-atts;
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          term-status CDATA #IMPLIED
 *          vocab CDATA #IMPLIED
 *          vocab-identifier CDATA #IMPLIED
 *          vocab-term CDATA #IMPLIED
 *          vocab-term-identifier CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/term.html
 *
 * Note: The term element contains the word or phrase being defined.
 * It supports mixed content including text and inline formatting elements.
 * Multiple terms can be associated with the same definition.
 *
 * Example:
 * <term>DNA</term>
 * <term vocab="MeSH" vocab-identifier="D004247">Deoxyribonucleic acid</term>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Term {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 용어의 고유 식별자.
     * EN: Unique identifier for this term.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 참조 ID / Reference ID
     *
     * KR: 다른 요소를 가리키는 참조 식별자.
     * EN: Reference identifier pointing to another element.
     *
     * DTD: rid IDREFS #IMPLIED
     * Required: NO
     */
    private String rid;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 용어의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this term.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 용어 상태 / Term status
     *
     * KR: 용어의 상태 (preferred, accepted, deprecated 등).
     * EN: Status of the term (preferred, accepted, deprecated, etc.).
     *
     * DTD: term-status CDATA #IMPLIED
     * Required: NO
     *
     * Example: "preferred", "accepted", "deprecated", "obsolete"
     */
    private String termStatus;

    /**
     * 어휘집 / Vocabulary
     *
     * KR: 이 용어가 속한 어휘집 이름.
     * EN: Name of the vocabulary this term belongs to.
     *
     * DTD: vocab CDATA #IMPLIED
     * Required: NO
     *
     * Example: "MeSH", "ICD-10", "SNOMED CT"
     */
    private String vocab;

    /**
     * 어휘집 식별자 / Vocabulary identifier
     *
     * KR: 어휘집의 고유 식별자 (URI 등).
     * EN: Unique identifier for the vocabulary (URI, etc.).
     *
     * DTD: vocab-identifier CDATA #IMPLIED
     * Required: NO
     */
    private String vocabIdentifier;

    /**
     * 어휘집 용어 / Vocabulary term
     *
     * KR: 어휘집에서의 공식 용어.
     * EN: Official term in the vocabulary.
     *
     * DTD: vocab-term CDATA #IMPLIED
     * Required: NO
     */
    private String vocabTerm;

    /**
     * 어휘집 용어 식별자 / Vocabulary term identifier
     *
     * KR: 어휘집에서의 용어 식별자.
     * EN: Term identifier in the vocabulary.
     *
     * DTD: vocab-term-identifier CDATA #IMPLIED
     * Required: NO
     *
     * Example: "D004247" (MeSH ID for DNA)
     */
    private String vocabTermIdentifier;

    /**
     * 용어 텍스트 / Term text
     *
     * KR: 용어의 텍스트 콘텐츠.
     * EN: Text content of the term.
     *
     * DTD: #PCDATA | %all-phrase;
     * Required: NO (can be empty)
     *
     * Example: "DNA", "Deoxyribonucleic acid", "PCR"
     */
    private String value;
}
