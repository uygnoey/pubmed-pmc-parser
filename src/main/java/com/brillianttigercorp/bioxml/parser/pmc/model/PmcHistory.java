package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * History / 이력
 *
 * KR: 논문 이력 (접수일, 수정일, 승인일 등)
 * EN: Article history (received, revised, accepted dates, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcHistory {
    private java.util.List<PmcDate> dates;
    private String value;
}
