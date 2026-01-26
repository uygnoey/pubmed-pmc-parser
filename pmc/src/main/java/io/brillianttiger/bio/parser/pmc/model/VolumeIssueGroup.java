package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS volume-issue-group 요소 / JATS volume-issue-group element
 *
 * <p>DTD: {@code <!ELEMENT volume-issue-group MIXED>}</p>
 *
 * <p>
 * KR: volume-issue-group 요소<br>
 * EN: volume-issue-group element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/volume-issue-group.html">
 *      JATS volume-issue-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeIssueGroup {

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
