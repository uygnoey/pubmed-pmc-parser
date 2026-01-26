package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Hour / 시
 *
 * DTD: <!ELEMENT Hour (#PCDATA)>
 *
 * KR: 시각 정보 - 시
 * EN: Time information - hour
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hour {

    /**
     * 시 / Hour
     */
    private String value;
}
