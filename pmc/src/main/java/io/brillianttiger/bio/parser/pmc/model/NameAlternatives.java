package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NameAlternatives / 이름 대체
 *
 * KR: 여러 형태의 이름
 * EN: Multiple forms of name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameAlternatives {
    private java.util.List<Name> names;
    private java.util.List<StringName> stringNames;
}
