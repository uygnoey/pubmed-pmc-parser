package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersonGroup / 인물 그룹
 *
 * KR: 인물 그룹 (저자, 편집자 등)
 * EN: Person group (authors, editors, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonGroup {
    private String personGroupType;
    private java.util.List<PersonName> names;
    private java.util.List<Collab> collabs;
    private java.util.List<Etal> etals;
}
