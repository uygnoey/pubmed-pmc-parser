package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersonalNameSubject / 인물 주제
 *
 * DTD: <!ELEMENT PersonalNameSubject (LastName, ForeName?, Initials?, Suffix?)>
 *
 * KR: 논문 주제가 되는 인물
 * EN: Person who is subject of article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalNameSubject {

    /**
     * 성 / Last name
     */
    private LastName lastName;

    /**
     * 이름 / First name
     */
    private ForeName foreName;

    /**
     * 이니셜 / Initials
     */
    private Initials initials;

    /**
     * 접미사 / Suffix
     */
    private Suffix suffix;
}
