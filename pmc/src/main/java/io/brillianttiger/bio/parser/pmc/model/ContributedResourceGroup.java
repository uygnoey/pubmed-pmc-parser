package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS contributed-resource-group 요소 / JATS contributed-resource-group element
 *
 * <p>DTD: {@code <!ELEMENT contributed-resource-group MIXED>}</p>
 *
 * <p>
 * KR: contributed-resource-group 요소<br>
 * EN: contributed-resource-group element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/contributed-resource-group.html">
 *      JATS contributed-resource-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributedResourceGroup {

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
