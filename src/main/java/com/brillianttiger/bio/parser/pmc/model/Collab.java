package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Collab / 협력 저자
 *
 * DTD: <!ELEMENT collab (#PCDATA | %collab-elements;)*>
 *
 * KR: 단체 저자 또는 협력 그룹
 * EN: Collective author or collaboration group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collab {

    /**
     * 협력 저자명 / Collaboration name
     */
    private String value;

    /**
     * 기여자 목록 / Contributor list
     */
    private List<Contrib> contributors;
}
