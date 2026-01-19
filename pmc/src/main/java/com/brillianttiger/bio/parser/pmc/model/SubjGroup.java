package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SubjGroup / 주제 그룹
 *
 * KR: 논문의 주제 분류를 나타내는 그룹.
 *     계층적 구조를 지원하며, 하위 주제 그룹을 포함할 수 있음.
 * EN: Group representing article subject classification.
 *     Supports hierarchical structure and can contain sub-subject groups.
 *
 * DTD: <!ELEMENT subj-group (
 *          (subject | compound-subject)+,
 *          subj-group*
 *      )>
 *
 * DTD: <!ATTLIST subj-group
 *          subj-group-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/subj-group.html
 *
 * Note: This is a recursive structure - a subj-group can contain other subj-groups.
 *
 * Examples:
 * <subj-group subj-group-type="heading">
 *   <subject>Research Article</subject>
 * </subj-group>
 *
 * <subj-group subj-group-type="discipline">
 *   <compound-subject>
 *     <compound-subject-part>Biological Sciences</compound-subject-part>
 *     <compound-subject-part>Molecular Biology</compound-subject-part>
 *   </compound-subject>
 *   <subj-group>
 *     <subject>Genetics</subject>
 *   </subj-group>
 * </subj-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjGroup {

    /**
     * 주제 그룹 유형 / Subject group type
     *
     * KR: 주제 그룹의 유형 (heading, discipline, category 등).
     * EN: Type of subject group (heading, discipline, category, etc.).
     *
     * DTD: subj-group-type CDATA #IMPLIED
     *
     * Common values:
     * - "heading": 제목 분류 / Heading classification
     * - "discipline": 학문 분야 / Academic discipline
     * - "category": 카테고리 / Category
     * - "series": 시리즈 / Series
     * - "topic": 주제 / Topic
     */
    private String subjGroupType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 주제 그룹의 특정 용도 또는 목적.
     * EN: Specific use or purpose of the subject group.
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 주제 목록 / Subject list
     *
     * KR: 이 그룹에 속하는 단순 주제 목록.
     * EN: List of simple subjects in this group.
     *
     * DTD: subject+
     */
    private List<Subject> subjects;

    /**
     * 복합 주제 목록 / Compound subject list
     *
     * KR: 이 그룹에 속하는 복합 주제 목록.
     *     계층적 주제 분류를 표현할 때 사용.
     * EN: List of compound subjects in this group.
     *     Used for hierarchical subject classification.
     *
     * DTD: compound-subject+
     */
    private List<CompoundSubject> compoundSubjects;

    /**
     * 하위 주제 그룹 목록 (재귀 구조) / Sub-subject group list (recursive structure)
     *
     * KR: 이 그룹 내의 하위 주제 그룹들.
     *     계층적 주제 분류를 위한 재귀 구조.
     * EN: Sub-subject groups within this group.
     *     Recursive structure for hierarchical subject classification.
     *
     * DTD: subj-group*
     *
     * Example:
     * Biology > Molecular Biology > Genetics
     */
    private List<SubjGroup> subGroups;
}
