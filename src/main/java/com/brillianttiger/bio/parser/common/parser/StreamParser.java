package com.brillianttiger.bio.parser.common.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * StreamParser / 스트리밍 파서 인터페이스
 *
 * KR: 대용량 파일을 위한 스트리밍 파서 인터페이스.
 * EN: Streaming parser interface for large files.
 *
 * @param <T> parsed item type
 */
public interface StreamParser<T> {

    /**
     * 파일을 스트리밍 방식으로 파싱 / Parse file in streaming mode
     *
     * KR: 각 아이템이 파싱될 때마다 handler가 호출됨.
     * EN: Handler is called for each parsed item.
     *
     * @param path XML file path
     * @param handler handler for each parsed item
     * @return number of processed items
     * @throws Exception if parsing error occurs
     */
    long parseStream(Path path, Consumer<T> handler) throws Exception;

    /**
     * 파일을 스트리밍 방식으로 파싱 (배치 처리) / Parse file in streaming mode (batch processing)
     *
     * @param path XML file path
     * @param batchSize batch size
     * @param handler batch processing handler
     * @return number of processed items
     * @throws Exception if parsing error occurs
     */
    long parseStreamBatch(Path path, int batchSize, Consumer<List<T>> handler)
            throws Exception;
}
