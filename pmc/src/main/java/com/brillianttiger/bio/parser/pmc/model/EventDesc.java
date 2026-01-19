package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS event-desc 요소 / JATS event-desc element
 *
 * <p>DTD: {@code <!ELEMENT event-desc (#PCDATA)>}</p>
 *
 * <p>
 * KR: event-desc 요소<br>
 * EN: event-desc element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/event-desc.html">
 *      JATS event-desc Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDesc {

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
