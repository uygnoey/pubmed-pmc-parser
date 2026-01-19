package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KwdGroup / 키워드 그룹
 *
 * KR: 논문 키워드 그룹. JATS 1.4 완전 준수 모델.
 * EN: Article keyword group. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT kwd-group (
 *          (label | title)*,
 *          (kwd | compound-kwd | nested-kwd | x | unstructured-kwd-group)*
 *      )>
 *
 * DTD: <!ATTLIST kwd-group
 *          assigning-authority CDATA #IMPLIED
 *          kwd-group-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          vocab CDATA #IMPLIED
 *          vocab-identifier CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/kwd-group.html
 *
 * Note: kwd-group-type common values:
 * - author: Author-supplied keywords (저자 제공 키워드)
 * - mesh: Medical Subject Headings (MeSH 키워드)
 * - lcsh: Library of Congress Subject Headings (LC 주제명표)
 * - abbreviations: List of abbreviations (약어 목록)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KwdGroup {

    /**
     * 할당 기관 / Assigning authority
     *
     * KR: 키워드를 할당한 기관이나 조직.
     * EN: Organization or agency that assigned the keywords.
     *
     * DTD: assigning-authority CDATA #IMPLIED
     *
     * Examples: "NLM", "publisher", "author"
     */
    private String assigningAuthority;

    /**
     * 키워드 그룹 유형 / Keyword group type
     *
     * KR: 키워드 그룹의 유형 (author, mesh, lcsh 등).
     * EN: Type of keyword group (author, mesh, lcsh, etc.).
     *
     * DTD: kwd-group-type CDATA #IMPLIED
     *
     * Common values:
     * - author: Author-supplied keywords
     * - mesh: Medical Subject Headings
     * - lcsh: Library of Congress Subject Headings
     * - abbreviations: List of abbreviations
     */
    private String kwdGroupType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 키워드 그룹의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this keyword group (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 어휘 체계 / Vocabulary system
     *
     * KR: 사용된 어휘 체계나 분류 시스템.
     * EN: Vocabulary system or classification scheme used.
     *
     * DTD: vocab CDATA #IMPLIED
     *
     * Examples: "MeSH", "LCSH", "NCIt"
     */
    private String vocab;

    /**
     * 어휘 체계 식별자 / Vocabulary identifier
     *
     * KR: 어휘 체계의 고유 식별자 (URI 등).
     * EN: Unique identifier for the vocabulary system (URI, etc.).
     *
     * DTD: vocab-identifier CDATA #IMPLIED
     *
     * Example: "https://www.nlm.nih.gov/mesh/"
     */
    private String vocabIdentifier;

    /**
     * 언어 / Language
     *
     * KR: 키워드의 언어 (ISO 639 코드).
     * EN: Language of keywords (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     *
     * Examples: "en", "ko", "ja", "zh"
     */
    private String xmlLang;

    /**
     * 레이블 목록 / Label list
     *
     * KR: 키워드 그룹의 레이블 목록.
     * EN: Label list of keyword group.
     *
     * DTD: label*
     * Required: NO (0 or more)
     */
    private List<Label> labels;

    /**
     * 제목 목록 / Title list
     *
     * KR: 키워드 그룹의 제목 목록.
     * EN: Title list of keyword group.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private List<Title> titles;

    /**
     * 키워드 목록 / Keyword list
     *
     * KR: 단순 키워드 목록.
     * EN: Simple keyword list.
     *
     * DTD: kwd*
     * Required: NO (0 or more)
     */
    private List<Kwd> keywords;

    /**
     * 복합 키워드 목록 / Compound keyword list
     *
     * KR: 복합 키워드 목록 (여러 부분으로 구성).
     * EN: Compound keyword list (composed of multiple parts).
     *
     * DTD: compound-kwd*
     * Required: NO (0 or more)
     */
    private List<CompoundKwd> compoundKeywords;

    /**
     * 중첩 키워드 목록 / Nested keyword list
     *
     * KR: 계층적 중첩 키워드 목록.
     * EN: Hierarchical nested keyword list.
     *
     * DTD: nested-kwd*
     * Required: NO (0 or more)
     */
    private List<NestedKwd> nestedKeywords;
}
