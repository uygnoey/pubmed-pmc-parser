package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS principal-investigator 요소 / JATS principal-investigator element
 *
 * <p>DTD: {@code <!ELEMENT principal-investigator MIXED>}</p>
 *
 * <p>
 * KR: principal-investigator 요소<br>
 * EN: principal-investigator element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/principal-investigator.html">
 *      JATS principal-investigator Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalInvestigator {

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
