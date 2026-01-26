package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ContribGroup / 기여자 그룹
 *
 * KR: 논문의 저자, 편집자 등 기여자 그룹. JATS 1.4 완전 준수 모델.
 * EN: Article contributors group (authors, editors, etc.). Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT contrib-group (
 *          (contrib | address | aff | aff-alternatives | author-comment |
 *           bio | email | etal | ext-link | fn | on-behalf-of | role |
 *           uri | xref | x)+
 *      )>
 *
 * DTD: <!ATTLIST contrib-group
 *          content-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/contrib-group.html
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContribGroup {

    /**
     * 콘텐츠 유형 / Content type
     * DTD: content-type CDATA #IMPLIED
     */
    private String contentType;

    /**
     * 특정 용도 / Specific use
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 기여자 목록 (1개 이상 필수) / Contributor list (at least one required)
     */
    private List<Contrib> contributors;

    /**
     * 상호참조 목록 / Cross-reference list
     */
    private List<Xref> xrefs;

    /**
     * 소속 목록 / Affiliation list
     */
    private List<Aff> affiliations;
}
