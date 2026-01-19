package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS issue-subtitle 요소 / JATS issue-subtitle element
 *
 * <p>DTD: {@code <!ELEMENT issue-subtitle (#PCDATA)>}</p>
 *
 * <p>
 * KR: issue-subtitle 요소<br>
 * EN: issue-subtitle element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/issue-subtitle.html">
 *      JATS issue-subtitle Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueSubtitle {

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
