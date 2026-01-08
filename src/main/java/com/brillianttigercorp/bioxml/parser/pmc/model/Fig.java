package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Fig / 그림
 *
 * DTD: <!ELEMENT fig ((%fig-model;)*)>
 * DTD: <!ATTLIST fig
 *          fig-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          position (anchor | float | margin) "float">
 *
 * KR: 논문 그림
 * EN: Article figure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fig {

    /**
     * 그림 유형 / Figure type
     */
    private String figType;

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
     * 그래픽 목록 / Graphic list
     */
    private List<Graphic> graphics;
}
