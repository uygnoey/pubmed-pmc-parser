package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VolumeTitle / 권 제목
 *
 * DTD: <!ELEMENT VolumeTitle (#PCDATA)>
 *
 * KR: 권 제목
 * EN: Volume title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeTitle {

    /**
     * 권 제목 / Volume title
     */
    private String value;
}
