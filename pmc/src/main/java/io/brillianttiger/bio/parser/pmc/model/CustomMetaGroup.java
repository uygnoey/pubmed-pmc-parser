package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomMetaGroup / 사용자 정의 메타 그룹
 *
 * KR: 사용자 정의 메타데이터 그룹
 * EN: Custom metadata group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMetaGroup {
    private java.util.List<CustomMeta> customMetas;
    private String value;
}
