package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PersonalNameSubjectList / 인물 주제 목록
 *
 * DTD: <!ELEMENT PersonalNameSubjectList (PersonalNameSubject+)>
 *
 * KR: 논문 주제가 되는 인물 목록
 * EN: List of persons who are subject of article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalNameSubjectList {

    /**
     * 인물 주제 목록 / Personal name subject list
     */
    private List<PersonalNameSubject> personalNameSubjects;
}
