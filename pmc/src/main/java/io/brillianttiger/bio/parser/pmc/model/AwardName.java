package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS award-name 요소 / JATS award-name element
 *
 * <p>DTD: {@code <!ELEMENT award-name (#PCDATA)>}</p>
 *
 * <p>
 * KR: award-name 요소<br>
 * EN: award-name element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/award-name.html">
 *      JATS award-name Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardName {

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
