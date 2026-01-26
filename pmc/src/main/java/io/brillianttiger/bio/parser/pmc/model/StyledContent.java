package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS styled-content 요소 / JATS styled-content element
 *
 * <p>DTD: {@code <!ELEMENT styled-content (#PCDATA)>}</p>
 *
 * <p>
 * KR: styled-content 요소<br>
 * EN: styled-content element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/styled-content.html">
 *      JATS styled-content Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StyledContent {

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
