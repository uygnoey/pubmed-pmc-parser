package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment / 코멘트
 *
 * KR: 참조문헌 코멘트
 * EN: Reference comment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String value;
}
