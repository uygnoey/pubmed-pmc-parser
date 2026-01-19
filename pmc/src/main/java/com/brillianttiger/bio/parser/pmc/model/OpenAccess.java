package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS open-access 요소 / JATS open-access element
 *
 * <p>DTD: {@code <!ELEMENT open-access (#PCDATA)>}</p>
 *
 * <p>
 * KR: 오픈 액세스 정보<br>
 * EN: Open access information
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/open-access.html">
 *      JATS open-access Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccess {

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
