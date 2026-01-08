package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Source / 출처
 *
 * KR: 출처 (저널명, 책명 등)
 * EN: Source (journal name, book name, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    private String value;
}
