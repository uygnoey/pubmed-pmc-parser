package com.brillianttiger.bio.parser.common.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StreamParserTest / StreamParser 인터페이스 테스트
 *
 * KR: StreamParser 인터페이스의 default 메서드와 ProgressCallback 테스트.
 * EN: Test for StreamParser interface default methods and ProgressCallback.
 */
class StreamParserTest {

    /**
     * TestStreamParser / 테스트용 StreamParser 구현체
     */
    static class TestStreamParser implements StreamParser<String> {
        private final List<String> items;

        public TestStreamParser(List<String> items) {
            this.items = items;
        }

        @Override
        public long parseStream(Path path, Consumer<String> handler) throws Exception {
            // 모든 아이템 처리
            items.forEach(handler);
            return items.size();
        }

        @Override
        public long parseStreamBatch(Path path, int batchSize, Consumer<List<String>> handler) throws Exception {
            // 배치로 아이템 처리
            List<String> batch = new ArrayList<>();
            for (String item : items) {
                batch.add(item);
                if (batch.size() >= batchSize) {
                    handler.accept(new ArrayList<>(batch));
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                handler.accept(batch);
            }
            return items.size();
        }
    }

    @Test
    @DisplayName("parseStream with ProgressCallback - default method")
    public void test01_parseStreamWithProgress() throws Exception {
        // Test default parseStream(path, handler, progress) method
        List<String> items = Arrays.asList("item1", "item2", "item3");
        TestStreamParser parser = new TestStreamParser(items);

        List<String> collected = new ArrayList<>();
        AtomicLong progressCount = new AtomicLong(0);
        AtomicInteger callCount = new AtomicInteger(0);

        StreamParser.ProgressCallback progress = (count, total, percentage) -> {
            progressCount.set(count);
            callCount.incrementAndGet();
        };

        long result = parser.parseStream(null, collected::add, progress);

        assertEquals(3, result);
        assertEquals(3, collected.size());
        assertEquals(3, progressCount.get());
        assertEquals(1, callCount.get()); // progress called once at end
    }

    @Test
    @DisplayName("parseStream with null ProgressCallback")
    public void test02_parseStreamWithNullProgress() throws Exception {
        // Test parseStream with null progress (should not throw exception)
        List<String> items = Arrays.asList("a", "b");
        TestStreamParser parser = new TestStreamParser(items);

        List<String> collected = new ArrayList<>();

        long result = parser.parseStream(null, collected::add, null);

        assertEquals(2, result);
        assertEquals(2, collected.size());
    }

    @Test
    @DisplayName("parseStreamBatch with ProgressCallback - default method")
    public void test03_parseStreamBatchWithProgress() throws Exception {
        // Test default parseStreamBatch(path, batchSize, handler, progress) method
        List<String> items = Arrays.asList("1", "2", "3", "4", "5");
        TestStreamParser parser = new TestStreamParser(items);

        List<List<String>> batches = new ArrayList<>();
        AtomicLong progressCount = new AtomicLong(0);

        StreamParser.ProgressCallback progress = (count, total, percentage) -> {
            progressCount.set(count);
        };

        long result = parser.parseStreamBatch(null, 2, batches::add, progress);

        assertEquals(5, result);
        assertEquals(3, batches.size()); // [1,2], [3,4], [5]
        assertEquals(5, progressCount.get());
    }

    @Test
    @DisplayName("parseStreamBatch with null ProgressCallback")
    public void test04_parseStreamBatchWithNullProgress() throws Exception {
        // Test parseStreamBatch with null progress (should not throw exception)
        List<String> items = Arrays.asList("x", "y", "z");
        TestStreamParser parser = new TestStreamParser(items);

        List<List<String>> batches = new ArrayList<>();

        long result = parser.parseStreamBatch(null, 1, batches::add, null);

        assertEquals(3, result);
        assertEquals(3, batches.size());
    }

    @Test
    @DisplayName("ProgressCallback.onProgress(long) - default method")
    public void test05_progressCallbackSimpleForm() {
        // Test default onProgress(processedCount) method
        AtomicLong countArg = new AtomicLong(0);
        AtomicLong totalArg = new AtomicLong(0);
        AtomicInteger percentageArg = new AtomicInteger(0);

        StreamParser.ProgressCallback callback = (count, total, percentage) -> {
            countArg.set(count);
            totalArg.set(total);
            percentageArg.set((int) percentage);
        };

        callback.onProgress(42);

        assertEquals(42, countArg.get());
        assertEquals(-1, totalArg.get());
        assertEquals(-1, percentageArg.get());
    }

    @Test
    @DisplayName("ProgressCallback.calculatePercentage - static method")
    public void test06_calculatePercentageValid() {
        // Test static calculatePercentage method with valid inputs
        double percentage = StreamParser.ProgressCallback.calculatePercentage(50, 100);
        assertEquals(50.0, percentage, 0.01);

        double percent25 = StreamParser.ProgressCallback.calculatePercentage(25, 100);
        assertEquals(25.0, percent25, 0.01);

        double percent100 = StreamParser.ProgressCallback.calculatePercentage(100, 100);
        assertEquals(100.0, percent100, 0.01);
    }

    @Test
    @DisplayName("ProgressCallback.calculatePercentage - over 100%")
    public void test07_calculatePercentageOver100() {
        // Test calculatePercentage when processedCount > estimatedTotal (should cap at 100)
        double percentage = StreamParser.ProgressCallback.calculatePercentage(150, 100);
        assertEquals(100.0, percentage, 0.01);
    }

    @Test
    @DisplayName("ProgressCallback.calculatePercentage - zero total")
    public void test08_calculatePercentageZeroTotal() {
        // Test calculatePercentage with estimatedTotal = 0 (should return -1)
        double percentage = StreamParser.ProgressCallback.calculatePercentage(50, 0);
        assertEquals(-1.0, percentage, 0.01);
    }

    @Test
    @DisplayName("ProgressCallback.calculatePercentage - negative total")
    public void test09_calculatePercentageNegativeTotal() {
        // Test calculatePercentage with estimatedTotal < 0 (should return -1)
        double percentage = StreamParser.ProgressCallback.calculatePercentage(50, -10);
        assertEquals(-1.0, percentage, 0.01);
    }

    @Test
    @DisplayName("ProgressCallback - all three parameters")
    public void test10_progressCallbackFullForm() {
        // Test full onProgress(count, total, percentage) method
        AtomicLong countArg = new AtomicLong(0);
        AtomicLong totalArg = new AtomicLong(0);
        AtomicInteger percentageArg = new AtomicInteger(0);

        StreamParser.ProgressCallback callback = (count, total, percentage) -> {
            countArg.set(count);
            totalArg.set(total);
            percentageArg.set((int) percentage);
        };

        callback.onProgress(50, 100, 50.0);

        assertEquals(50, countArg.get());
        assertEquals(100, totalArg.get());
        assertEquals(50, percentageArg.get());
    }
}
