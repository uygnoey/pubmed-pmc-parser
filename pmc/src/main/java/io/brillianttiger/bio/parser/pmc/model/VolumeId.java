package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VolumeId / 권 ID
 *
 * KR: 권 식별자
 * EN: Volume identifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeId {
    private String pubIdType;
    private String value;
}
