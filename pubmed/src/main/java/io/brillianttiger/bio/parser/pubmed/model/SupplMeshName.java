package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SupplMeshName / 보충 MeSH명
 *
 * DTD: <!ELEMENT SupplMeshName (#PCDATA)>
 * DTD: <!ATTLIST SupplMeshName
 *          Type (Disease | Protocol | Organism) #REQUIRED
 *          UI CDATA #REQUIRED>
 *
 * KR: 보충 MeSH 개념
 * EN: Supplementary MeSH concept
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplMeshName {

    /**
     * 유형: Disease | Protocol | Organism (필수) / Type (required)
     */
    private SupplMeshNameType type;

    /**
     * UI (고유 식별자, 필수) / UI (unique identifier, required)
     */
    private String ui;

    /**
     * 보충 MeSH명 / Supplementary MeSH name
     */
    private String value;
}
