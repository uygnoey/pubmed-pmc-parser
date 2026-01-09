package com.brillianttiger.bio.parser.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GzipUtils 테스트 / GzipUtils Test
 *
 * KR: GZip 파일 처리 유틸리티 테스트
 * EN: Tests for GZip file handling utilities
 */
class GzipUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void testConstants() {
        // 상수 접근 가능성 확인 / Verify constant accessibility
        assertEquals(65536, GzipUtils.BUFFER_SIZE);
        assertEquals(0x1f8b, GzipUtils.GZIP_MAGIC);
    }

    @Test
    void testIsGzipFileByExtension() {
        // 확장자 기반 GZip 파일 감지 / Extension-based GZip file detection
        Path gzipPath = Path.of("test.xml.gz");
        Path xmlPath = Path.of("test.xml");
        Path upperCasePath = Path.of("TEST.XML.GZ");

        assertTrue(GzipUtils.isGzipFile(gzipPath));
        assertFalse(GzipUtils.isGzipFile(xmlPath));
        assertTrue(GzipUtils.isGzipFile(upperCasePath)); // 대소문자 무시 / case-insensitive

        // null 처리 / null handling
        assertFalse(GzipUtils.isGzipFile(null));
    }

    @Test
    void testIsGzippedByteArray() {
        // 매직 넘버 기반 GZip 감지 (바이트 배열) / Magic number-based GZip detection (byte array)

        // 유효한 GZip 매직 넘버 / Valid GZip magic number
        byte[] gzipHeader = {(byte) 0x1f, (byte) 0x8b, 0x08, 0x00};
        assertTrue(GzipUtils.isGzipped(gzipHeader));

        // 잘못된 매직 넘버 / Invalid magic number
        byte[] xmlHeader = {'<', '?', 'x', 'm', 'l'};
        assertFalse(GzipUtils.isGzipped(xmlHeader));

        // 너무 짧은 배열 / Array too short
        byte[] shortHeader = {(byte) 0x1f};
        assertFalse(GzipUtils.isGzipped(shortHeader));

        // null 처리 / null handling
        assertFalse(GzipUtils.isGzipped((byte[]) null));

        // 빈 배열 / Empty array
        assertFalse(GzipUtils.isGzipped(new byte[0]));
    }

    @Test
    void testIsGzippedByMagicNumber() throws IOException {
        // 실제 GZip 파일 생성 / Create actual GZip file
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gz");

        Files.writeString(xmlFile, "<?xml version=\"1.0\"?><root>test</root>");
        GzipUtils.compress(xmlFile, gzipFile);

        // 매직 넘버로 GZip 파일 확인 / Verify GZip file by magic number
        assertTrue(GzipUtils.isGzipFileByMagicNumber(gzipFile));
        assertTrue(GzipUtils.isGzipped(gzipFile)); // 별칭 메서드 / alias method

        // 일반 XML 파일 / Regular XML file
        assertFalse(GzipUtils.isGzipFileByMagicNumber(xmlFile));
        assertFalse(GzipUtils.isGzipped(xmlFile));

        // null 및 존재하지 않는 파일 / null and non-existent file
        assertFalse(GzipUtils.isGzipFileByMagicNumber(null));
        assertFalse(GzipUtils.isGzipFileByMagicNumber(tempDir.resolve("nonexistent.gz")));
    }

    @Test
    void testCreateInputStream() throws IOException {
        // GZip 파일 생성 / Create GZip file
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gz");
        String content = "<?xml version=\"1.0\"?><root>Hello, World!</root>";

        Files.writeString(xmlFile, content);
        GzipUtils.compress(xmlFile, gzipFile);

        // GZip 자동 감지 스트림 / Auto-detect GZip stream
        try (InputStream is = GzipUtils.createInputStream(gzipFile)) {
            String read = new String(is.readAllBytes());
            assertEquals(content, read);
        }

        // 일반 파일 읽기 / Read regular file
        try (InputStream is = GzipUtils.createInputStream(xmlFile)) {
            String read = new String(is.readAllBytes());
            assertEquals(content, read);
        }
    }

    @Test
    void testOpenGzipInputStream() throws IOException {
        // 강제 GZip 스트림 열기 / Force open GZip stream
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gz");
        String content = "Test content for GZip";

        Files.writeString(xmlFile, content);
        GzipUtils.compress(xmlFile, gzipFile);

        try (InputStream is = GzipUtils.openGzipInputStream(gzipFile)) {
            String read = new String(is.readAllBytes());
            assertEquals(content, read);
        }
    }

    @Test
    void testCompress() throws IOException {
        // 파일 압축 / Compress file
        Path inputFile = tempDir.resolve("input.txt");
        Path gzipFile = tempDir.resolve("output.txt.gz");

        // 반복되는 큰 콘텐츠로 압축 효과 확인 / Use large repetitive content for compression benefit
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("This is a test line for compression. Line number: ").append(i).append("\n");
        }
        String content = sb.toString();

        Files.writeString(inputFile, content);
        GzipUtils.compress(inputFile, gzipFile);

        // 압축 파일 존재 확인 / Verify compressed file exists
        assertTrue(Files.exists(gzipFile));
        assertTrue(Files.size(gzipFile) < Files.size(inputFile)); // 압축되어야 함 / should be compressed

        // 압축 해제 후 내용 검증 / Verify content after decompression
        try (InputStream is = GzipUtils.openGzipInputStream(gzipFile)) {
            String read = new String(is.readAllBytes());
            assertEquals(content, read);
        }
    }

    @Test
    void testDecompress() throws IOException {
        // 압축 및 압축 해제 / Compress and decompress
        Path originalFile = tempDir.resolve("original.txt");
        Path gzipFile = tempDir.resolve("compressed.txt.gz");
        Path decompressedFile = tempDir.resolve("decompressed.txt");
        String content = "Original content for decompression test.\nMultiple lines\nTo verify integrity.";

        Files.writeString(originalFile, content);
        GzipUtils.compress(originalFile, gzipFile);
        GzipUtils.decompress(gzipFile, decompressedFile);

        // 압축 해제 파일 검증 / Verify decompressed file
        assertTrue(Files.exists(decompressedFile));
        String decompressed = Files.readString(decompressedFile);
        assertEquals(content, decompressed);
    }

    @Test
    void testRemoveGzipExtension() {
        // .gz 확장자 제거 / Remove .gz extension
        Path gzipPath = Path.of("/path/to/file.xml.gz");
        Path expected = Path.of("/path/to/file.xml");

        assertEquals(expected, GzipUtils.removeGzipExtension(gzipPath));

        // 대소문자 무시 / Case-insensitive
        Path upperPath = Path.of("/path/to/FILE.XML.GZ");
        Path upperExpected = Path.of("/path/to/FILE.XML");
        assertEquals(upperExpected, GzipUtils.removeGzipExtension(upperPath));

        // .gz가 아닌 파일 / Non-.gz file
        Path xmlPath = Path.of("/path/to/file.xml");
        assertEquals(xmlPath, GzipUtils.removeGzipExtension(xmlPath));

        // null 처리 / null handling
        assertNull(GzipUtils.removeGzipExtension(null));

        // 부모 디렉토리 없는 경우 / No parent directory
        Path simpleGzip = Path.of("file.gz");
        Path simpleExpected = Path.of("file");
        assertEquals(simpleExpected, GzipUtils.removeGzipExtension(simpleGzip));
    }

    @Test
    void testAddGzipExtension() {
        // .gz 확장자 추가 / Add .gz extension
        Path xmlPath = Path.of("/path/to/file.xml");
        Path expected = Path.of("/path/to/file.xml.gz");

        assertEquals(expected, GzipUtils.addGzipExtension(xmlPath));

        // 이미 .gz 확장자가 있는 경우 / Already has .gz extension
        Path gzipPath = Path.of("/path/to/file.xml.gz");
        assertEquals(gzipPath, GzipUtils.addGzipExtension(gzipPath));

        // 대소문자 무시 / Case-insensitive
        Path upperPath = Path.of("/path/to/FILE.XML.GZ");
        assertEquals(upperPath, GzipUtils.addGzipExtension(upperPath));

        // null 처리 / null handling
        assertNull(GzipUtils.addGzipExtension(null));

        // 부모 디렉토리 없는 경우 / No parent directory
        Path simple = Path.of("file.xml");
        Path simpleExpected = Path.of("file.xml.gz");
        assertEquals(simpleExpected, GzipUtils.addGzipExtension(simple));
    }

    @Test
    void testEstimateCompressedSize() {
        // 압축 크기 추정 / Estimate compressed size
        long originalSize = 1000000L; // 1MB
        long estimated = GzipUtils.estimateCompressedSize(originalSize);

        // 10:1 비율로 추정 / Estimated at 10:1 ratio
        assertEquals(100000L, estimated);
    }

    @Test
    void testEstimateDecompressedSize() {
        // 압축 해제 크기 추정 / Estimate decompressed size
        long compressedSize = 100000L; // 100KB
        long estimated = GzipUtils.estimateDecompressedSize(compressedSize);

        // 10:1 비율 역산 / Reverse 10:1 ratio
        assertEquals(1000000L, estimated);
    }

    @Test
    void testOpenReader() throws IOException {
        // GZip Reader 열기 / Open GZip Reader
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gz");
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>한글 테스트</root>";

        Files.writeString(xmlFile, content);
        GzipUtils.compress(xmlFile, gzipFile);

        // GZip 파일 읽기 / Read GZip file
        try (var reader = GzipUtils.openReader(gzipFile)) {
            String firstLine = reader.readLine();
            assertTrue(firstLine.contains("<?xml"));
        }

        // 일반 파일 읽기 / Read regular file
        try (var reader = GzipUtils.openReader(xmlFile)) {
            String firstLine = reader.readLine();
            assertTrue(firstLine.contains("<?xml"));
        }
    }

    @Test
    void testOpenGzipWriter() throws IOException {
        // GZip Writer 열기 / Open GZip Writer
        Path gzipFile = tempDir.resolve("output.txt.gz");
        String content = "Test content with UTF-8: 한글 테스트";

        // GZip으로 쓰기 / Write to GZip
        try (var writer = GzipUtils.openGzipWriter(gzipFile)) {
            writer.write(content);
        }

        // 읽어서 검증 / Read and verify
        try (var reader = GzipUtils.openReader(gzipFile)) {
            String read = reader.readLine();
            assertEquals(content, read);
        }
    }

    @Test
    void testCompressDecompressRoundTrip() throws IOException {
        // 왕복 테스트 / Round-trip test
        Path originalFile = tempDir.resolve("original.xml");
        Path gzipFile = tempDir.resolve("compressed.xml.gz");
        Path decompressedFile = tempDir.resolve("decompressed.xml");

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "  <element>한글 content</element>\n" +
                "  <element>English content</element>\n" +
                "  <element>日本語 content</element>\n" +
                "</root>";

        Files.writeString(originalFile, content);

        // 압축 / Compress
        GzipUtils.compress(originalFile, gzipFile);
        assertTrue(Files.exists(gzipFile));

        // 압축 해제 / Decompress
        GzipUtils.decompress(gzipFile, decompressedFile);
        assertTrue(Files.exists(decompressedFile));

        // 내용 동일성 검증 / Verify content identity
        String decompressed = Files.readString(decompressedFile);
        assertEquals(content, decompressed);
    }

    @Test
    void testNullSafetyForPathOperations() {
        // null 안전성 테스트 / null safety test
        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.openInputStream(null));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.openGzipInputStream(null));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.openGzipOutputStream(null));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.decompress(null, tempDir.resolve("output.txt")));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.decompress(tempDir.resolve("input.gz"), null));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.compress(null, tempDir.resolve("output.gz")));

        assertThrows(IllegalArgumentException.class,
            () -> GzipUtils.compress(tempDir.resolve("input.txt"), null));
    }
}
