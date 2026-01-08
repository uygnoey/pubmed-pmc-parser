package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address / 주소
 *
 * KR: 주소
 * EN: Address
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String value;
}
