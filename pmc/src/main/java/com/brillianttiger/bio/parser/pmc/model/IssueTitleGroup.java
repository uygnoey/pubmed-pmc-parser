package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS issue-title-group 요소 / JATS issue-title-group element
 *
 * <p>DTD: {@code <!ELEMENT issue-title-group MIXED>}</p>
 *
 * <p>
 * KR: issue-title-group 요소<br>
 * EN: issue-title-group element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/issue-title-group.html">
 *      JATS issue-title-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueTitleGroup {

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
