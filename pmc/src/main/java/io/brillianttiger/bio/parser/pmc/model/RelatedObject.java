package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RelatedObject / 관련 객체
 *
 * KR: 관련 객체
 * EN: Related object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedObject {
    private String documentType;
    private String id;
    private String xlinkHref;
    private String value;
}
