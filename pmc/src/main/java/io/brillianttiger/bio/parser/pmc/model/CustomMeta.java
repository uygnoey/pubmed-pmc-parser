package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomMeta / 사용자 정의 메타
 *
 * KR: 사용자 정의 메타데이터
 * EN: Custom metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMeta {
    private String id;
    private MetaName metaName;
    private MetaValue metaValue;
}
