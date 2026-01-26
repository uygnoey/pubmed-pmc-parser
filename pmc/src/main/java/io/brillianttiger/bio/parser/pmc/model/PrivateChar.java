package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS private-char 요소 / JATS private-char element
 *
 * <p>DTD: {@code <!ELEMENT private-char MIXED>}</p>
 *
 * <p>
 * KR: private-char 요소<br>
 * EN: private-char element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/private-char.html">
 *      JATS private-char Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChar {

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
