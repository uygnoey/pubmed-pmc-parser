package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS pub-date-not-available 요소 / JATS pub-date-not-available element
 *
 * <p>DTD: {@code <!ELEMENT pub-date-not-available MIXED>}</p>
 *
 * <p>
 * KR: pub-date-not-available 요소<br>
 * EN: pub-date-not-available element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/pub-date-not-available.html">
 *      JATS pub-date-not-available Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubDateNotAvailable {

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
