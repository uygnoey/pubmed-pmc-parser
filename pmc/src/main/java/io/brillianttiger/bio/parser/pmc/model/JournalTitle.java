package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalTitle / 저널 제목
 *
 * KR: 저널 제목
 * EN: Journal title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalTitle {
    private String value;
}
