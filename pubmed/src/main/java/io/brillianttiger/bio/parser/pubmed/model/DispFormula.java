package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DispFormula / 표시 수식
 *
 * DTD: <!ELEMENT DispFormula (mml:math)>
 *
 * KR: MathML 형식의 표시 수식
 * EN: Display formula in MathML format
 *
 * NOTE: MathML 3.0을 사용하여 수학 수식을 표현
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispFormula {

    /**
     * MathML 수식 내용 / MathML formula content
     *
     * KR: MathML 3.0 형식의 수학 수식 (원본 XML 문자열로 저장)
     * EN: Mathematical formula in MathML 3.0 format (stored as raw XML string)
     */
    private String mathMLContent;
}
