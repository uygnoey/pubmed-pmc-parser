package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalId / 저널 ID
 *
 * DTD: <!ELEMENT journal-id (#PCDATA)>
 * DTD: <!ATTLIST journal-id journal-id-type CDATA #IMPLIED>
 *
 * KR: 저널의 고유 식별자 (NLM-TA, pmc, publisher-id 등)
 * EN: Journal unique identifier (NLM-TA, pmc, publisher-id, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalId {

    /**
     * 저널 ID 유형 / Journal ID type
     */
    private String journalIdType;

    /**
     * 저널 ID 값 / Journal ID value
     */
    private String value;
}
