package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthorComment / 저자 코멘트
 *
 * KR: 저자 코멘트
 * EN: Author comment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorComment {
    private String value;
}
