package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Caption / 캡션
 *
 * KR: 그림/테이블 캡션
 * EN: Figure/Table caption
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caption {
    private Title title;
    private java.util.List<P> paragraphs;
}
