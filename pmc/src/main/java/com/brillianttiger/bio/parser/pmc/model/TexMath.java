package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TexMath / TeX 수식
 *
 * KR: TeX 형식의 수학 표현. JATS 1.4 DTD 완전 준수 모델.
 * EN: TeX formatted mathematical expression. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT tex-math (#PCDATA)>
 *
 * DTD: <!ATTLIST tex-math
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          notation (LaTeX | TEX | TeX) "TeX"
 *          specific-use CDATA #IMPLIED
 *          version CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/tex-math.html
 *
 * Example:
 * <tex-math notation="LaTeX">
 *     \frac{-b \pm \sqrt{b^2 - 4ac}}{2a}
 * </tex-math>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TexMath {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: TeX 수식의 콘텐츠 유형.
     * EN: Content type of the TeX math.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     */
    private String id;

    /**
     * 표기법 / Notation
     *
     * KR: TeX 표기법 유형.
     * EN: Type of TeX notation.
     *
     * DTD: notation (LaTeX | TEX | TeX) "TeX"
     * Required: NO
     * Default: "TeX"
     *
     * Values: LaTeX, TEX, TeX
     */
    @Builder.Default
    private String notation = "TeX";

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 버전 / Version
     *
     * KR: TeX 버전 정보.
     * EN: TeX version information.
     *
     * DTD: version CDATA #IMPLIED
     * Required: NO
     */
    private String version;

    // ========== Content / 내용 ==========

    /**
     * TeX 수식 내용 / TeX math content
     *
     * KR: TeX 형식의 수학 표현 문자열.
     * EN: Mathematical expression string in TeX format.
     *
     * DTD: #PCDATA
     *
     * Example: "\frac{-b \pm \sqrt{b^2 - 4ac}}{2a}"
     */
    private String content;
}
