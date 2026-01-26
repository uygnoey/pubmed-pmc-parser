package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BookDocumentSet / 도서 문서 세트 (최상위 루트 요소)
 *
 * DTD: <!ELEMENT BookDocumentSet (BookDocument*, DeleteDocument?)>
 *
 * KR: 도서 문서 XML 파일의 최상위 컨테이너. 도서 문서들과 삭제된 문서 정보를 포함
 * EN: Top-level container for book document XML file. Contains book documents and deleted document info
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDocumentSet {

    /**
     * 도서 문서 목록 (0개 이상) / Book document list (zero or more)
     */
    private List<BookDocument> bookDocuments;

    /**
     * 삭제된 문서 정보 (선택적) / Deleted document information (optional)
     */
    private DeleteDocument deleteDocument;
}
