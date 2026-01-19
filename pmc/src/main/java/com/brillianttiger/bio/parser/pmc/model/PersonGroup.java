package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PersonGroup / 인물 그룹
 *
 * KR: 인물 그룹 (저자, 편집자 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Person group (authors, editors, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT person-group ((%name.class; | aff | aff-alternatives | collab | collab-alternatives |
 *                              etal | name-alternatives | role | string-name)*)>
 *
 * DTD: <!ATTLIST person-group
 *          id ID #IMPLIED
 *          person-group-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/person-group.html
 *
 * Example:
 * <person-group person-group-type="author">
 *   <name><surname>Smith</surname><given-names>John A.</given-names></name>
 *   <name><surname>Doe</surname><given-names>Jane B.</given-names></name>
 *   <etal/>
 * </person-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonGroup {

    // ========== Attributes / 속성 ==========

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     */
    private String id;

    /**
     * 인물 그룹 유형 / Person group type
     *
     * KR: 인물 그룹의 역할 (저자, 편집자 등).
     * EN: Role of the person group (author, editor, etc.).
     *
     * DTD: person-group-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: author, editor, translator, guest-editor, compiler, inventor, assignee
     */
    private String personGroupType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XML Base / XML base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML language
     *
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 이름 목록 / Name list
     *
     * KR: 개인 이름 목록.
     * EN: List of person names.
     *
     * DTD: %name.class;*
     * Required: NO (0 or more)
     */
    private List<PersonName> names;

    /**
     * 문자열 이름 목록 / String name list
     *
     * KR: 문자열 형태의 이름 목록.
     * EN: List of names in string format.
     *
     * DTD: string-name*
     * Required: NO (0 or more)
     */
    private List<StringName> stringNames;

    /**
     * 협력 기관 목록 / Collaboration list
     *
     * KR: 협력 기관/그룹 목록.
     * EN: List of collaborating organizations/groups.
     *
     * DTD: collab*
     * Required: NO (0 or more)
     */
    private List<Collab> collabs;

    /**
     * 소속 목록 / Affiliation list
     *
     * KR: 소속 정보 목록.
     * EN: List of affiliations.
     *
     * DTD: aff*
     * Required: NO (0 or more)
     */
    private List<Aff> affiliations;

    /**
     * 역할 목록 / Role list
     *
     * KR: 개인의 역할 목록.
     * EN: List of person roles.
     *
     * DTD: role*
     * Required: NO (0 or more)
     */
    private List<Role> roles;

    /**
     * et al. 표시 / Et al. indicator
     *
     * KR: "et al." 표시 (추가 저자 있음을 나타냄).
     * EN: Et al. indicator (indicates additional authors).
     *
     * DTD: etal?
     * Required: NO
     */
    private Etal etal;

    /**
     * et al. 목록 / Et al. list
     *
     * KR: "et al." 표시 목록 (여러 개 있을 경우).
     * EN: List of et al. indicators (when multiple exist).
     *
     * DTD: etal*
     * Required: NO (0 or more)
     */
    private List<Etal> etals;
}
