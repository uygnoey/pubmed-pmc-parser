package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role / 역할
 *
 * KR: 저자 역할
 * EN: Author role
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String value;
}
