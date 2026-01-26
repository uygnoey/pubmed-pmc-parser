package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublisherLoc / 출판사 위치
 *
 * KR: 출판사 위치
 * EN: Publisher location
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherLoc {
    private String value;
}
