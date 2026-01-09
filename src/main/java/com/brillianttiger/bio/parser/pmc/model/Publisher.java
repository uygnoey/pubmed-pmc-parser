package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Publisher / 출판사
 *
 * KR: 저널의 출판사 정보를 나타내는 요소.
 *     출판사명과 위치를 포함.
 * EN: Element representing journal publisher information.
 *     Includes publisher name(s) and location(s).
 *
 * DTD: <!ELEMENT publisher (publisher-name+, publisher-loc*)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/publisher.html
 *
 * Note: publisher-name은 1개 이상 필수(+), publisher-loc은 선택적(*)
 *
 * Examples:
 * <publisher>
 *   <publisher-name>American Society for Biochemistry and Molecular Biology</publisher-name>
 *   <publisher-loc>Bethesda, MD</publisher-loc>
 * </publisher>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    /**
     * 출판사명 목록 (1개 이상 필수) / Publisher name list (at least one required)
     *
     * KR: 출판사의 공식 명칭 목록.
     *     여러 언어나 변형 명칭이 있을 수 있음.
     * EN: List of publisher's official names.
     *     May include multiple languages or name variants.
     *
     * DTD: publisher-name+
     * Required: YES (at least one)
     *
     * Examples:
     * - "American Society for Biochemistry and Molecular Biology"
     * - "Nature Publishing Group"
     * - "Elsevier"
     */
    private List<PublisherName> publisherNames;

    /**
     * 출판사 위치 목록 / Publisher location list
     *
     * KR: 출판사의 소재지 목록.
     *     도시, 국가 등의 위치 정보.
     * EN: List of publisher's locations.
     *     Location information such as city, country, etc.
     *
     * DTD: publisher-loc*
     * Required: NO
     *
     * Examples:
     * - "Bethesda, MD"
     * - "London, UK"
     * - "Amsterdam, Netherlands"
     */
    private List<PublisherLoc> publisherLocs;
}
