package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubjGroup / 주제 그룹
 *
 * KR: 주제 분류 그룹
 * EN: Subject group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjGroup {
    private String subjGroupType;
    private java.util.List<String> subjects;
}
