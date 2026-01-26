package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AbbrevJournalTitle / 저널 약어 제목
 *
 * KR: 저널 약어 제목
 * EN: Abbreviated journal title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbbrevJournalTitle {
    private String abbrevType;
    private String value;
}
