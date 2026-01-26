package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SecMeta / 섹션 메타
 *
 * KR: 섹션 메타데이터
 * EN: Section metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecMeta {
    private java.util.List<Kwd> keywords;
    private String value;
}
