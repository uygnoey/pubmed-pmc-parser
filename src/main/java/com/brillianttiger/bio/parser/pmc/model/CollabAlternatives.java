package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CollabAlternatives / 협력자 대체
 *
 * KR: 여러 형태의 협력자명
 * EN: Multiple forms of collaboration name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollabAlternatives {
    private java.util.List<Collab> collabs;
}
