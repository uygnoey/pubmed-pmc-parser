package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS license-p 요소 / JATS license-p element
 *
 * <p>DTD: {@code <!ELEMENT license-p (#PCDATA | %inline-elements;)*>}</p>
 *
 * <p>
 * KR: 라이선스 단락<br>
 * EN: License paragraph
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/license-p.html">
 *      JATS license-p Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseP {

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
