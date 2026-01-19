package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalSubtitle / 저널 부제
 *
 * KR: 저널 부제
 * EN: Journal subtitle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalSubtitle {
    private String value;
}
