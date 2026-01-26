package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS milestone-end 요소 / JATS milestone-end element
 *
 * <p>DTD: {@code <!ELEMENT milestone-end MIXED>}</p>
 *
 * <p>
 * KR: milestone-end 요소<br>
 * EN: milestone-end element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/milestone-end.html">
 *      JATS milestone-end Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneEnd {

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
