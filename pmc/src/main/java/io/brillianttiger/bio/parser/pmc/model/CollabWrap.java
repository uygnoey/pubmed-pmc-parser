package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS collab-wrap 요소 / JATS collab-wrap element
 *
 * <p>DTD: {@code <!ELEMENT collab-wrap MIXED>}</p>
 *
 * <p>
 * KR: collab-wrap 요소<br>
 * EN: collab-wrap element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/collab-wrap.html">
 *      JATS collab-wrap Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollabWrap {

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
