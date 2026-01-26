package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CompoundKwd / 복합 키워드
 *
 * KR: 여러 부분으로 구성된 복합 키워드. JATS 1.4 완전 준수 모델.
 * EN: Compound keyword composed of multiple parts. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT compound-kwd (compound-kwd-part+)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/compound-kwd.html
 *
 * Note: Used for keywords that consist of multiple distinct parts, often seen in
 * controlled vocabularies like MeSH where a keyword might have:
 * - A main heading
 * - A subheading
 * - A classification code
 *
 * Example:
 * <compound-kwd>
 *   <compound-kwd-part content-type="code">C14.907.585</compound-kwd-part>
 *   <compound-kwd-part content-type="heading">Hypertension</compound-kwd-part>
 *   <compound-kwd-part content-type="subheading">drug therapy</compound-kwd-part>
 * </compound-kwd>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompoundKwd {

    /**
     * 복합 키워드 부분 목록 / Compound keyword part list (REQUIRED)
     *
     * KR: 복합 키워드를 구성하는 부분 목록 (최소 1개 필수).
     * EN: List of parts that make up the compound keyword (at least one required).
     *
     * DTD: compound-kwd-part+
     * Required: YES (1 or more)
     *
     * Note: A compound keyword must have at least one part, but typically has 2-3 parts
     * representing different aspects of the keyword (e.g., code, term, modifier).
     */
    private List<CompoundKwdPart> parts;
}
