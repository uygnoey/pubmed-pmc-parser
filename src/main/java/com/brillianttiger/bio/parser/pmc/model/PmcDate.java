package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date / 날짜
 *
 * KR: 날짜
 * EN: Date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcDate {
    private String dateType;
    private Day day;
    private Month month;
    private Season season;
    private Year year;
}
