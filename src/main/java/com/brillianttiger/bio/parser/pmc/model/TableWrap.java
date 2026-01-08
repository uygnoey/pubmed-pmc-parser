package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TableWrap / 테이블 래퍼
 *
 * DTD: <!ELEMENT table-wrap ((%table-wrap-model;)*)>
 * DTD: <!ATTLIST table-wrap
 *          id ID #IMPLIED
 *          position (anchor | float | margin) "float">
 *
 * KR: 논문 테이블 래퍼
 * EN: Article table wrapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableWrap {

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * 위치: anchor | float | margin (기본값: "float") / Position (default: "float")
     */
    @Builder.Default
    private String position = "float";

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 캡션 / Caption
     */
    private Caption caption;

    /**
     * 테이블 / Table
     */
    private Table table;
}
