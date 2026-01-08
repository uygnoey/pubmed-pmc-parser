package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Email / 이메일
 *
 * KR: 이메일 주소
 * EN: Email address
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String value;
}
