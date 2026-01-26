package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS time-stamp 요소 / JATS time-stamp element
 *
 * <p>DTD: {@code <!ELEMENT time-stamp (#PCDATA)>}</p>
 *
 * <p>
 * KR: time-stamp 요소<br>
 * EN: time-stamp element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/time-stamp.html">
 *      JATS time-stamp Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeStamp {

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
