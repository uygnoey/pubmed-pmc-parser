package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CompoundSubjectPart / 복합 주제 파트
 *
 * KR: 복합 주제의 구성 요소를 나타내는 요소.
 *     Mixed content를 포함할 수 있음.
 * EN: Element representing a component of a compound subject.
 *     Can contain mixed content.
 *
 * DTD: <!ELEMENT compound-subject-part (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/compound-subject-part.html
 *
 * Examples:
 * <compound-subject-part>Biochemistry</compound-subject-part>
 * <compound-subject-part>Cell Biology</compound-subject-part>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompoundSubjectPart {

    /**
     * 파트 내용 / Part content
     *
     * KR: 복합 주제 파트의 텍스트 내용.
     * EN: Text content of the compound subject part.
     *
     * DTD: (#PCDATA | %all-phrase;)*
     */
    private String content;
}
