package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Label / 레이블
 *
 * KR: 요소의 레이블 (번호, 식별자 등)
 * EN: Element label (number, identifier, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    private String value;
}
