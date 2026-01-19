package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS milestone-start 요소 / JATS milestone-start element
 *
 * <p>DTD: {@code <!ELEMENT milestone-start MIXED>}</p>
 *
 * <p>
 * KR: milestone-start 요소<br>
 * EN: milestone-start element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/milestone-start.html">
 *      JATS milestone-start Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneStart {

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
