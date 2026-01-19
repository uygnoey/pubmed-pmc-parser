package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PageCount / 쪽 수
 *
 * DTD: <!ELEMENT page-count EMPTY>
 * DTD: <!ATTLIST page-count
 *          count CDATA #REQUIRED
 *          id ID #IMPLIED
 *          xml:base CDATA #IMPLIED
 *      >
 *
 * KR: 인쇄 또는 PDF 간행물의 총 쪽 수
 * EN: Total page count of printed or PDF publication
 *
 * Usage: 메타데이터의 <counts> 요소 내에만 나타남
 * Note: 각 쪽 또는 부분 쪽을 1로 계산
 * Related: <fpage>, <lpage>, <elocation-id>, <page-range>
 * Citation: 인용문헌 내에서는 <size> 사용
 * Electronic: 전자 전용 자료는 전통적으로 페이지 수 미포함
 *
 * Example:
 * <counts>
 *   <page-count count="6"/>
 * </counts>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageCount {
    /**
     * KR: 총 쪽 수 (필수)
     * EN: Total page count (required)
     */
    private String count;

    /**
     * KR: ID 속성
     * EN: ID attribute
     */
    private String id;

    /**
     * KR: XML base
     * EN: XML base
     */
    private String xmlBase;
}
