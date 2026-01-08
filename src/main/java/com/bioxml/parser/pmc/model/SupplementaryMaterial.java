package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SupplementaryMaterial / 보충 자료
 *
 * DTD: <!ELEMENT supplementary-material (label?, caption?, abstract*, kwd-group*, alt-text*,
 *                                        long-desc*, email*, ext-link*, uri*,
 *                                        (%display-back-matter.class;)*, attrib?, permissions?)>
 * DTD: <!ATTLIST supplementary-material
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          mime-subtype CDATA #IMPLIED
 *          mimetype CDATA #IMPLIED
 *          xlink:href CDATA #IMPLIED>
 *
 * KR: 논문 보충 자료
 * EN: Article supplementary material
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementaryMaterial {

    /**
     * 콘텐츠 유형 / Content type
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * MIME 서브타입 / MIME subtype
     */
    private String mimeSubtype;

    /**
     * MIME 타입 / MIME type
     */
    private String mimetype;

    /**
     * XLink href / XLink href
     */
    private String xlinkHref;

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 캡션 / Caption
     */
    private Caption caption;

    /**
     * 초록 목록 / Abstract list
     */
    private List<PmcAbstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     */
    private List<KwdGroup> kwdGroups;

    /**
     * 대체 텍스트 목록 / Alternative text list
     */
    private List<AltText> altTexts;

    /**
     * 긴 설명 목록 / Long description list
     */
    private List<LongDesc> longDescs;

    /**
     * 이메일 목록 / Email list
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     */
    private List<Uri> uris;

    /**
     * 속성 / Attribution
     */
    private Attrib attrib;

    /**
     * 권한 / Permissions
     */
    private Permissions permissions;

    /**
     * 텍스트 값 / Text value
     */
    private String value;
}
