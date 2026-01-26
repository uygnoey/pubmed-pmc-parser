package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CopyrightStatement / 저작권 문구
 *
 * KR: 저작권 문구
 * EN: Copyright statement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyrightStatement {
    private String value;
}
