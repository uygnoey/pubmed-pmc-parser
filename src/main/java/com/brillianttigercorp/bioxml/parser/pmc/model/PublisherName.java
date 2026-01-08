package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublisherName / 출판사명
 *
 * KR: 출판사명
 * EN: Publisher name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherName {
    private String value;
}
