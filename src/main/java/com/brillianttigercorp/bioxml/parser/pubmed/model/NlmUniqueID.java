package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NlmUniqueID / NLM 고유 식별자
 *
 * DTD: <!ELEMENT NlmUniqueID (#PCDATA)>
 *
 * KR: NLM(National Library of Medicine)에서 저널에 부여한 고유 ID
 * EN: Unique ID assigned to journal by NLM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NlmUniqueID {

    /**
     * NLM 고유 ID / NLM unique ID
     */
    private String value;
}
