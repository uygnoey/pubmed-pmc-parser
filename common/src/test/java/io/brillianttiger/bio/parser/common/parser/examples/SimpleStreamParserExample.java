package io.brillianttiger.bio.parser.common.parser.examples;

import io.brillianttiger.bio.parser.common.parser.StreamParser;
import io.brillianttiger.bio.parser.common.parser.XmlParserBase;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SimpleStreamParserExample / 간단한 스트리밍 파서 예시
 *
 * KR: StreamParser 인터페이스의 간단한 구현 예시.
 *     단순한 XML 구조를 파싱하여 StreamParser 사용법을 보여줌.
 * EN: Simple streaming parser example implementation.
 *     Parses simple XML structure to demonstrate StreamParser usage.
 *
 * Example XML:
 * <pre>{@code
 * <ArticleSet>
 *   <Article>
 *     <Id>1</Id>
 *     <Title>Sample Title</Title>
 *     <Author>John Doe</Author>
 *     <Year>2024</Year>
 *   </Article>
 * </ArticleSet>
 * }</pre>
 */
public class SimpleStreamParserExample extends XmlParserBase
        implements StreamParser<SimpleArticle> {

    /**
     * 스트리밍 방식으로 XML 파일 파싱 / Parse XML file in streaming mode
     *
     * KR: 각 Article 요소를 파싱하여 handler에 전달.
     *     메모리 효율적으로 대용량 파일 처리 가능.
     * EN: Parse each Article element and pass to handler.
     *     Memory-efficient for large files.
     *
     * @param path XML 파일 경로 / XML file path
     * @param handler 각 Article 처리 핸들러 / Handler for each article
     * @return 처리된 Article 개수 / Number of processed articles
     * @throws Exception 파싱 오류 시 / On parsing error
     */
    @Override
    public long parseStream(Path path, Consumer<SimpleArticle> handler) throws Exception {
        long count = 0;

        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("Article".equals(localName)) {
                        SimpleArticle article = parseArticle(reader);
                        handler.accept(article);
                        count++;
                    }
                }
            }

            reader.close();
        }

        return count;
    }

    /**
     * 배치 방식으로 XML 파일 파싱 / Parse XML file in batch mode
     *
     * KR: Article을 배치 단위로 수집하여 handler에 전달.
     *     데이터베이스 bulk insert 등에 유용.
     * EN: Collect articles in batches and pass to handler.
     *     Useful for database bulk inserts, etc.
     *
     * @param path XML 파일 경로 / XML file path
     * @param batchSize 배치 크기 / Batch size
     * @param handler 배치 처리 핸들러 / Batch handler
     * @return 처리된 Article 개수 / Number of processed articles
     * @throws Exception 파싱 오류 시 / On parsing error
     */
    @Override
    public long parseStreamBatch(Path path, int batchSize,
                                  Consumer<List<SimpleArticle>> handler) throws Exception {
        long count = 0;
        List<SimpleArticle> batch = new ArrayList<>(batchSize);

        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT &&
                        "Article".equals(reader.getLocalName())) {

                    SimpleArticle article = parseArticle(reader);
                    batch.add(article);
                    count++;

                    // 배치 크기 도달 시 처리 / Process when batch size reached
                    if (batch.size() >= batchSize) {
                        handler.accept(new ArrayList<>(batch));
                        batch.clear();
                    }
                }
            }

            // 남은 배치 처리 / Process remaining batch
            if (!batch.isEmpty()) {
                handler.accept(batch);
            }

            reader.close();
        }

        return count;
    }

    /**
     * ProgressCallback 포함 스트리밍 파싱 / Streaming with progress callback
     *
     * KR: 진행 상황을 실시간으로 보고하며 파싱.
     *     대용량 파일의 진행률을 UI에 표시할 때 유용.
     * EN: Parse with real-time progress reporting.
     *     Useful for displaying progress in UI for large files.
     *
     * @param path XML 파일 경로 / XML file path
     * @param handler 각 Article 처리 핸들러 / Handler for each article
     * @param progress 진행 상황 콜백 / Progress callback
     * @return 처리된 Article 개수 / Number of processed articles
     * @throws Exception 파싱 오류 시 / On parsing error
     */
    @Override
    public long parseStream(Path path, Consumer<SimpleArticle> handler,
                            ProgressCallback progress) throws Exception {
        long count = 0;

        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("Article".equals(localName)) {
                        SimpleArticle article = parseArticle(reader);
                        handler.accept(article);
                        count++;

                        // 진행 상황 보고 / Report progress
                        if (progress != null) {
                            // 전체 수를 모르므로 percentage는 -1
                            // Don't know total, so percentage is -1
                            progress.onProgress(count, -1, -1.0);
                        }
                    }
                }
            }

            reader.close();
        }

        return count;
    }

    /**
     * ProgressCallback 포함 배치 파싱 / Batch parsing with progress callback
     *
     * KR: 배치 단위로 처리하며 진행 상황 보고.
     * EN: Process in batches while reporting progress.
     *
     * @param path XML 파일 경로 / XML file path
     * @param batchSize 배치 크기 / Batch size
     * @param handler 배치 처리 핸들러 / Batch handler
     * @param progress 진행 상황 콜백 / Progress callback
     * @return 처리된 Article 개수 / Number of processed articles
     * @throws Exception 파싱 오류 시 / On parsing error
     */
    @Override
    public long parseStreamBatch(Path path, int batchSize,
                                  Consumer<List<SimpleArticle>> handler,
                                  ProgressCallback progress) throws Exception {
        long count = 0;
        List<SimpleArticle> batch = new ArrayList<>(batchSize);

        try (var is = openInputStream(path)) {
            XMLStreamReader reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT &&
                        "Article".equals(reader.getLocalName())) {

                    SimpleArticle article = parseArticle(reader);
                    batch.add(article);
                    count++;

                    if (batch.size() >= batchSize) {
                        handler.accept(new ArrayList<>(batch));

                        // 진행 상황 보고 / Report progress
                        if (progress != null) {
                            progress.onProgress(count, -1, -1.0);
                        }

                        batch.clear();
                    }
                }
            }

            // 남은 배치 처리 / Process remaining batch
            if (!batch.isEmpty()) {
                handler.accept(batch);

                if (progress != null) {
                    progress.onProgress(count, -1, -1.0);
                }
            }

            reader.close();
        }

        return count;
    }

    /**
     * Article 요소 파싱 / Parse Article element
     *
     * KR: <Article> 요소의 자식 요소들을 파싱하여 SimpleArticle 객체 생성.
     *     호출 시점: reader는 <Article> START_ELEMENT를 가리킴.
     * EN: Parse child elements of <Article> and create SimpleArticle object.
     *     On entry: reader points to <Article> START_ELEMENT.
     *
     * @param reader XML 스트림 리더 / XML stream reader
     * @return 파싱된 Article / Parsed article
     * @throws XMLStreamException XML 파싱 오류 시 / On XML parsing error
     */
    private SimpleArticle parseArticle(XMLStreamReader reader) throws XMLStreamException {
        SimpleArticle.SimpleArticleBuilder builder = SimpleArticle.builder();

        // <Article> 요소 내부를 순회 / Iterate inside <Article> element
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Id" -> builder.id(getElementText(reader));
                    case "Title" -> builder.title(getElementText(reader));
                    case "Author" -> builder.author(getElementText(reader));
                    case "Year" -> {
                        String yearStr = getElementText(reader);
                        if (yearStr != null && !yearStr.isEmpty()) {
                            builder.year(Integer.parseInt(yearStr));
                        }
                    }
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // </Article> 종료 태그를 만나면 파싱 완료
                // Found </Article> end tag, parsing complete
                if ("Article".equals(reader.getLocalName())) {
                    break;
                }
            }
        }

        return builder.build();
    }
}
