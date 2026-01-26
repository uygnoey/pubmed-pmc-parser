package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS ali:free-to-read 요소 / JATS ali:free-to-read element
 *
 * <p>DTD: {@code <!ELEMENT ali:free-to-read MIXED>}</p>
 *
 * <p>
 * KR: ali:free-to-read 요소<br>
 * EN: ali:free-to-read element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ali-free-to-read.html">
 *      JATS ali:free-to-read Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliFreeToRead {

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
