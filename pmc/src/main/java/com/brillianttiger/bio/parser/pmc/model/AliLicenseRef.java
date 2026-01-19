package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS ali:license-ref 요소 / JATS ali:license-ref element
 *
 * <p>DTD: {@code <!ELEMENT ali:license-ref MIXED>}</p>
 *
 * <p>
 * KR: ali:license-ref 요소<br>
 * EN: ali:license-ref element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ali-license-ref.html">
 *      JATS ali:license-ref Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliLicenseRef {

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

}
