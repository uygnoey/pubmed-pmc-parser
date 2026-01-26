package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CompoundSubject / 복합 주제
 *
 * KR: 여러 파트로 구성된 복합 주제를 나타내는 요소.
 *     예: "생화학 > 세포 생물학"과 같은 계층적 주제 분류.
 * EN: Element representing a compound subject made up of multiple parts.
 *     Example: hierarchical subject classification like "Biochemistry > Cell Biology".
 *
 * DTD: <!ELEMENT compound-subject (compound-subject-part+)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/compound-subject.html
 *
 * Examples:
 * <compound-subject>
 *   <compound-subject-part>Biological Sciences</compound-subject-part>
 *   <compound-subject-part>Molecular Biology</compound-subject-part>
 * </compound-subject>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompoundSubject {

    /**
     * 복합 주제 파트 목록 (1개 이상 필수) / Compound subject part list (at least one required)
     *
     * KR: 복합 주제를 구성하는 파트들.
     *     계층적 순서대로 나열됨.
     * EN: Parts that make up the compound subject.
     *     Listed in hierarchical order.
     *
     * DTD: compound-subject-part+
     * Required: YES (at least one)
     *
     * Example:
     * ["Biological Sciences", "Molecular Biology", "Genetics"]
     */
    private List<CompoundSubjectPart> parts;
}
