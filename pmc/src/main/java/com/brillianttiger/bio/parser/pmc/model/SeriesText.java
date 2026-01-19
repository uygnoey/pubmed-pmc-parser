package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SeriesText / 시리즈 텍스트
 *
 * KR: 시리즈 설명 텍스트
 * EN: Series description text
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeriesText {
    private String value;
}
