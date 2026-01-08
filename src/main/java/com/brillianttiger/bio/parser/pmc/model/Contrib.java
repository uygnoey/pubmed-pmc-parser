package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contrib / 기여자
 *
 * DTD: <!ELEMENT contrib (contrib-id*, name?, name-alternatives?, string-name?, collab?,
 *                        collab-alternatives?, anonymous?, degrees*, address*, aff*,
 *                        aff-alternatives*, author-comment?, bio?, email*, etal?, ext-link*,
 *                        fn*, on-behalf-of?, role*, uri*, xref*)>
 * DTD: <!ATTLIST contrib
 *          contrib-type (author | editor | guest-editor | collab | compiler | director |
 *                        inventor | reviewer | translator | series-editor) #IMPLIED
 *          corresp (yes | no) #IMPLIED
 *          deceased (yes | no) #IMPLIED
 *          equal-contrib (yes | no) #IMPLIED
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED>
 *
 * KR: 논문 기여자 (저자, 편집자 등)
 * EN: Article contributor (author, editor, etc.)
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
     */
    private String rid;

    /**
     * 기여자 ID 목록 / Contributor ID list
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
