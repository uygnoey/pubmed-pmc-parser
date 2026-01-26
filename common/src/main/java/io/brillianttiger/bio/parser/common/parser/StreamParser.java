package io.brillianttiger.bio.parser.common.parser;

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

    /**
     * 파일을 스트리밍 방식으로 파싱 (진행 상황 콜백 포함) / Parse file in streaming mode (with progress callback)
     *
     * KR: 각 아이템이 파싱될 때마다 handler와 progress가 호출됨.
     * EN: Both handler and progress are called for each parsed item.
     *
     * @param path XML file path
     * @param handler handler for each parsed item
     * @param progress progress callback (count, estimated total)
     * @return number of processed items
     * @throws Exception if parsing error occurs
     */
    default long parseStream(Path path, Consumer<T> handler, ProgressCallback progress) throws Exception {
        // 기본 구현: progress 무시하고 일반 parseStream 호출
        long count = 0;
        count = parseStream(path, item -> {
            handler.accept(item);
        });

        if (progress != null) {
            progress.onProgress(count);  // total unknown, use simple form
        }

        return count;
    }

    /**
     * 파일을 스트리밍 방식으로 파싱 (배치 처리, 진행 상황 콜백 포함) / Parse file in streaming mode (batch, with progress)
     *
     * KR: 각 배치가 처리될 때마다 handler와 progress가 호출됨.
     * EN: Both handler and progress are called for each batch.
     *
     * @param path XML file path
     * @param batchSize batch size
     * @param handler batch processing handler
     * @param progress progress callback (count, estimated total)
     * @return number of processed items
     * @throws Exception if parsing error occurs
     */
    default long parseStreamBatch(Path path, int batchSize, Consumer<List<T>> handler,
                                   ProgressCallback progress) throws Exception {
        // 기본 구현: progress 무시하고 일반 parseStreamBatch 호출
        long count = 0;
        count = parseStreamBatch(path, batchSize, batch -> {
            handler.accept(batch);
        });

        if (progress != null) {
            progress.onProgress(count);  // total unknown, use simple form
        }

        return count;
    }

    /**
     * ProgressCallback / 진행 상황 콜백 인터페이스
     *
     * KR: 파싱 진행 상황을 보고하기 위한 콜백 인터페이스.
     * EN: Callback interface for reporting parsing progress.
     */
    @FunctionalInterface
    interface ProgressCallback {

        /**
         * 진행 상황 업데이트 / Progress update
         *
         * KR: 처리된 아이템 수, 전체 예상 수, 진행률을 보고.
         * EN: Report processed item count, estimated total, and percentage.
         *
         * @param processedCount 처리된 아이템 수 / Number of processed items
         * @param estimatedTotal 예상 전체 수 (-1이면 알 수 없음) / Estimated total (-1 if unknown)
         * @param percentage 진행률 (0.0 ~ 100.0, -1이면 알 수 없음) / Percentage (0.0 ~ 100.0, -1 if unknown)
         */
        void onProgress(long processedCount, long estimatedTotal, double percentage);

        /**
         * 간단한 진행 상황 업데이트 (count만) / Simple progress update (count only)
         *
         * KR: 전체 수를 모를 때 사용
         * EN: Use when total is unknown
         *
         * @param processedCount 처리된 아이템 수 / Number of processed items
         */
        default void onProgress(long processedCount) {
            onProgress(processedCount, -1, -1.0);
        }

        /**
         * 진행률 계산 / Calculate progress percentage
         *
         * KR: 처리된 수와 전체 수로부터 진행률(0-100) 계산.
         * EN: Calculate progress percentage (0-100) from counts.
         *
         * @param processedCount 처리된 아이템 수 / Number of processed items
         * @param estimatedTotal 예상 전체 수 / Estimated total
         * @return 진행률 (0-100) / Progress percentage (0-100)
         */
        static double calculatePercentage(long processedCount, long estimatedTotal) {
            if (estimatedTotal <= 0) {
                return -1.0;
            }
            return Math.min(100.0, (processedCount * 100.0) / estimatedTotal);
        }
    }
}
