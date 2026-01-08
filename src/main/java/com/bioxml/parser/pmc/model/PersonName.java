package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PersonName / 개인명
 *
 * DTD: <!ELEMENT name (surname, given-names?, prefix?, suffix?)>
 * DTD: <!ELEMENT person-group ((name | collab | etal)+)>
 *
 * KR: element-citation 및 person-group에서 사용되는 개인 이름
 * EN: Personal name used in element-citation and person-group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonName {

    /**
     * 성 / Surname
     */
    private Surname surname;

    /**
     * 이름 / Given names
     */
    private GivenNames givenNames;

    /**
     * 접두사 / Prefix
     */
    private Prefix prefix;

    /**
     * 접미사 / Suffix
     */
    private Suffix suffix;
}
