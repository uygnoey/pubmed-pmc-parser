package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS processing-meta 요소 / JATS processing-meta element
 *
 * <p>DTD: {@code <!ELEMENT processing-meta (EMPTY)>}</p>
 *
 * <p>
 * KR: 처리 메타데이터<br>
 * EN: Processing metadata
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/processing-meta.html">
 *      JATS processing-meta Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingMeta {

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
