package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StringName / 문자열 이름
 *
 * KR: 구조화되지 않은 이름 (문자열)
 * EN: Unstructured name (string)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringName {
    private String value;
}
