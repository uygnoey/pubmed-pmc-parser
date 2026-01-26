package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contrib / 기여자
 *
 * KR: 논문 기여자 (저자, 편집자 등). JATS 1.4 완전 준수 모델.
 * EN: Article contributor (author, editor, etc.). Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT contrib (contrib-id | anonymous | collab | collab-alternatives |
 *          name | name-alternatives | string-name | degrees | address | aff |
 *          aff-alternatives | author-comment | bio | email | etal |
 *          ext-link | fn | on-behalf-of | role | uri | xref | x)*>
 *
 * DTD: <!ATTLIST contrib
 *          contrib-type CDATA #IMPLIED
 *          corresp (yes | no) #IMPLIED
 *          deceased (yes | no) #IMPLIED
 *          equal-contrib (yes | no) #IMPLIED
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/contrib.html
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contrib {

    /**
     * 기여자 유형 / Contributor type
     */
    private String contribType;

    /**
     * 교신 저자 여부: yes | no / Corresponding author flag
     */
    private String corresp;

    /**
     * 사망 여부: yes | no / Deceased flag
     */
    private String deceased;

    /**
     * 동등 기여 여부: yes | no / Equal contribution flag
     */
    private String equalContrib;

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * 참조 ID 목록 / Reference ID list
     * DTD: rid IDREFS #IMPLIED
     */
    private String rid;

    /**
     * 특정 용도 / Specific use
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * XLink actuate 속성 / XLink actuate attribute
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     */
    private String xlinkActuate;

    /**
     * XLink href 속성 / XLink href attribute
     * DTD: xlink:href CDATA #IMPLIED
     */
    private String xlinkHref;

    /**
     * XLink role 속성 / XLink role attribute
     * DTD: xlink:role CDATA #IMPLIED
     */
    private String xlinkRole;

    /**
     * XLink show 속성 / XLink show attribute
     * DTD: xlink:show (embed | new | none | other | replace) #IMPLIED
     */
    private String xlinkShow;

    /**
     * XLink title 속성 / XLink title attribute
     * DTD: xlink:title CDATA #IMPLIED
     */
    private String xlinkTitle;

    /**
     * XLink type 속성 / XLink type attribute
     * DTD: xlink:type (simple) #IMPLIED
     */
    private String xlinkType;

    /**
     * 기여자 ID 목록 / Contributor ID list
     * DTD: contrib-id*
     */
    private List<ContribId> contribIds;

    /**
     * 이름 (구조화) / Name (structured)
     */
    private Name name;

    /**
     * 이름 대체 / Name alternatives
     */
    private NameAlternatives nameAlternatives;

    /**
     * 문자열 이름 / String name
     */
    private StringName stringName;

    /**
     * 협력 저자 / Collaboration
     */
    private Collab collab;

    /**
     * 협력 대체 / Collaboration alternatives
     */
    private CollabAlternatives collabAlternatives;

    /**
     * 익명 / Anonymous
     */
    private Anonymous anonymous;

    /**
     * 학위 목록 / Degrees list
     */
    private List<Degrees> degrees;

    /**
     * 주소 목록 / Address list
     */
    private List<Address> addresses;

    /**
     * 소속 목록 / Affiliation list
     */
    private List<Aff> affiliations;

    /**
     * 소속 대체 목록 / Affiliation alternatives list
     */
    private List<AffAlternatives> affAlternatives;

    /**
     * 저자 코멘트 / Author comment
     */
    private AuthorComment authorComment;

    /**
     * 약력 / Biography
     */
    private Bio bio;

    /**
     * 이메일 목록 / Email list
     */
    private List<Email> emails;

    /**
     * Et al. / Et al.
     */
    private Etal etal;

    /**
     * 외부 링크 목록 / External link list
     */
    private List<ExtLink> extLinks;

    /**
     * 각주 목록 / Footnote list
     */
    private List<Fn> footnotes;

    /**
     * On behalf of / On behalf of
     */
    private OnBehalfOf onBehalfOf;

    /**
     * 역할 목록 / Role list
     */
    private List<Role> roles;

    /**
     * URI 목록 / URI list
     */
    private List<Uri> uris;

    /**
     * 상호참조 목록 / Cross-reference list
     */
    private List<Xref> xrefs;
}
