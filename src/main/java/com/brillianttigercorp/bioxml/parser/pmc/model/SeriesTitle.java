package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SeriesTitle / 시리즈 제목
 *
 * KR: 시리즈 제목
 * EN: Series title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeriesTitle {
    private String value;
}
