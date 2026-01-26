package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MeshHeadingList / MeSH 주제어 목록
 *
 * DTD: <!ELEMENT MeshHeadingList (MeshHeading+)>
 *
 * KR: MeSH 주제어 목록
 * EN: MeSH heading list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeshHeadingList {

    /**
     * MeSH 주제어 목록 / MeSH heading list
     */
    private List<MeshHeading> meshHeadings;
}
