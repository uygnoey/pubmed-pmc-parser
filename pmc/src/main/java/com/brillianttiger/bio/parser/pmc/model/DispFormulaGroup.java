package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS disp-formula-group 요소 / JATS disp-formula-group element
 *
 * <p>DTD: {@code <!ELEMENT disp-formula-group (label?, (disp-formula)+)>}</p>
 *
 * <p>
 * KR: 표시 수식 그룹<br>
 * EN: Display formula group
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/disp-formula-group.html">
 *      JATS disp-formula-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispFormulaGroup {

    /**
     * 텍스트 내용 / Text content
     */
    private String value;

    /**
     * 공통 속성: id
     */
    private String id;

    /**
     * 공통 속성: xml:lang
     */
    private String xmlLang;

    /**
     * 라벨
     */
    private String label;

    /**
     * 수식 목록 / Formulas
     */
    @Builder.Default
    private List<DispFormula> dispFormulas = new ArrayList<>();

}
