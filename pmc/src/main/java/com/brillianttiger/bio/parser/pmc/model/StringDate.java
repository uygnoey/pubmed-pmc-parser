package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StringDate / 문자열 날짜
 *
 * KR: 구조화되지 않은 날짜 (문자열)
 * EN: Unstructured date (string)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringDate {
    private String value;
}
