package com.brillianttiger.bio.parser.common.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GzipUtils / GZip 유틸리티
 *
 * KR: GZip 압축 파일 처리를 위한 유틸리티
 * EN: Utility for handling GZip compressed files
 */
public class GzipUtils {

    /**
     * 버퍼 크기: 64KB / Buffer size: 64KB
     *
     * KR: PubMed XML 파일 처리를 위한 최적 버퍼 크기
     * EN: Optimal buffer size for PubMed XML file processing
     */
    public static final int BUFFER_SIZE = 65536;  // 64KB

    /**
     * 기본 버퍼 크기 (하위 호환성) / Default buffer size (backward compatibility)
     */
    private static final int DEFAULT_BUFFER_SIZE = BUFFER_SIZE;

    /**
     * GZip 매직 넘버 / GZip magic number
     *
     * KR: GZip 파일의 시작 2바이트: 0x1f 0x8b
     * EN: First 2 bytes of GZip file: 0x1f 0x8b
     */
    public static final int GZIP_MAGIC = 0x1f8b;

    /**
     * InputStream 열기 (GZip 자동 처리) / Open InputStream (auto-handle GZip)
     *
     * KR: 파일 경로를 받아 InputStream을 반환. .gz 확장자면 자동으로 압축 해제
     * EN: Return InputStream from file path. Auto-decompress if .gz extension
     *
     * @param filePath 파일 경로 / File path
     * @return InputStream (버퍼링됨) / InputStream (buffered)
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static InputStream openInputStream(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        InputStream fileInputStream = Files.newInputStream(filePath);
        InputStream bufferedInputStream = new BufferedInputStream(fileInputStream, DEFAULT_BUFFER_SIZE);

        // GZip 파일 자동 감지 및 처리 / Auto-detect and handle GZip files
        if (isGzipFile(filePath)) {
            return new GZIPInputStream(bufferedInputStream, DEFAULT_BUFFER_SIZE);
        }

        return bufferedInputStream;
    }

    /**
     * InputStream 생성 (별칭 메서드) / Create InputStream (alias method)
     *
     * KR: openInputStream의 별칭. 더 직관적인 이름.
     *     GZip 자동 감지 및 64KB 버퍼 사용.
     * EN: Alias for openInputStream. More intuitive name.
     *     Auto-detect GZip and use 64KB buffer.
     *
     * @param filePath 파일 경로 / File path
     * @return InputStream (버퍼링됨, GZip 자동 처리) / InputStream (buffered, auto-handle GZip)
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static InputStream createInputStream(Path filePath) throws IOException {
        return openInputStream(filePath);
    }

    /**
     * InputStream 열기 (강제 GZip 처리) / Open InputStream (force GZip handling)
     *
     * KR: 확장자와 관계없이 GZip 스트림으로 열기
     * EN: Open as GZip stream regardless of extension
     *
     * @param filePath 파일 경로 / File path
     * @return GZIPInputStream (버퍼링됨) / GZIPInputStream (buffered)
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static InputStream openGzipInputStream(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        InputStream fileInputStream = Files.newInputStream(filePath);
        InputStream bufferedInputStream = new BufferedInputStream(fileInputStream, DEFAULT_BUFFER_SIZE);

        return new GZIPInputStream(bufferedInputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * OutputStream 열기 (GZip 압축) / Open OutputStream (GZip compression)
     *
     * KR: GZip 압축 OutputStream 반환
     * EN: Return GZip compressed OutputStream
     *
     * @param filePath 파일 경로 / File path
     * @return GZIPOutputStream (버퍼링됨) / GZIPOutputStream (buffered)
     * @throws IOException 파일 쓰기 오류 / File writing error
     */
    public static OutputStream openGzipOutputStream(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        OutputStream fileOutputStream = Files.newOutputStream(filePath);
        OutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, DEFAULT_BUFFER_SIZE);

        return new GZIPOutputStream(bufferedOutputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * GZip 파일 여부 확인 (확장자 기반) / Check if GZip file (extension-based)
     *
     * KR: 파일 확장자가 .gz인지 확인
     * EN: Check if file extension is .gz
     *
     * @param filePath 파일 경로 / File path
     * @return GZip 파일 여부 / Whether it's a GZip file
     */
    public static boolean isGzipFile(Path filePath) {
        if (filePath == null) {
            return false;
        }

        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.endsWith(".gz");
    }

    /**
     * GZip 파일 여부 확인 (매직 넘버 기반) / Check if GZip file (magic number-based)
     *
     * KR: 파일의 첫 2바이트를 읽어 GZip 매직 넘버(0x1f 0x8b) 확인
     * EN: Read first 2 bytes of file and check GZip magic number (0x1f 0x8b)
     *
     * @param filePath 파일 경로 / File path
     * @return GZip 파일 여부 / Whether it's a GZip file
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static boolean isGzipFileByMagicNumber(Path filePath) throws IOException {
        if (filePath == null || !Files.exists(filePath)) {
            return false;
        }

        try (InputStream in = new BufferedInputStream(Files.newInputStream(filePath), DEFAULT_BUFFER_SIZE)) {
            if (!in.markSupported()) {
                return false;
            }

            in.mark(2);
            int byte1 = in.read();
            int byte2 = in.read();
            in.reset();

            return ((byte1 << 8) | byte2) == GZIP_MAGIC;
        }
    }

    /**
     * GZip 파일 여부 확인 (별칭 메서드) / Check if GZip file (alias method)
     *
     * KR: isGzipFileByMagicNumber의 별칭. 더 간결한 이름.
     *     파일의 첫 2바이트(0x1f 0x8b)로 GZip 확인.
     * EN: Alias for isGzipFileByMagicNumber. More concise name.
     *     Check GZip by first 2 bytes (0x1f 0x8b).
     *
     * @param filePath 파일 경로 / File path
     * @return GZip 파일 여부 / Whether it's a GZip file
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static boolean isGzipped(Path filePath) throws IOException {
        return isGzipFileByMagicNumber(filePath);
    }

    /**
     * GZip 파일 여부 확인 (바이트 배열 기반) / Check if GZip file (byte array-based)
     *
     * KR: 바이트 배열의 첫 2바이트를 읽어 GZip 매직 넘버(0x1f 0x8b) 확인
     * EN: Read first 2 bytes of byte array and check GZip magic number (0x1f 0x8b)
     *
     * @param header 파일 헤더 바이트 배열 (최소 2바이트) / File header byte array (minimum 2 bytes)
     * @return GZip 파일 여부 / Whether it's a GZip file
     */
    public static boolean isGzipped(byte[] header) {
        if (header == null || header.length < 2) {
            return false;
        }

        // GZip 매직 넘버: 0x1f 0x8b / GZip magic number: 0x1f 0x8b
        int byte1 = header[0] & 0xFF;
        int byte2 = header[1] & 0xFF;

        return ((byte1 << 8) | byte2) == GZIP_MAGIC;
    }

    /**
     * 파일 압축 해제 / Decompress file
     *
     * KR: GZip 압축 파일을 압축 해제하여 새 파일로 저장
     * EN: Decompress GZip file and save to new file
     *
     * @param gzipFile 압축 파일 경로 / Compressed file path
     * @param outputFile 출력 파일 경로 / Output file path
     * @throws IOException 파일 처리 오류 / File processing error
     */
    public static void decompress(Path gzipFile, Path outputFile) throws IOException {
        if (gzipFile == null || outputFile == null) {
            throw new IllegalArgumentException("File paths cannot be null");
        }

        try (InputStream in = openGzipInputStream(gzipFile);
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(outputFile), DEFAULT_BUFFER_SIZE)) {

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 파일 압축 / Compress file
     *
     * KR: 파일을 GZip 형식으로 압축
     * EN: Compress file to GZip format
     *
     * @param inputFile 입력 파일 경로 / Input file path
     * @param gzipFile 압축 파일 경로 / Compressed file path
     * @throws IOException 파일 처리 오류 / File processing error
     */
    public static void compress(Path inputFile, Path gzipFile) throws IOException {
        if (inputFile == null || gzipFile == null) {
            throw new IllegalArgumentException("File paths cannot be null");
        }

        try (InputStream in = new BufferedInputStream(Files.newInputStream(inputFile), DEFAULT_BUFFER_SIZE);
             OutputStream out = openGzipOutputStream(gzipFile)) {

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 압축된 파일 크기 추정 / Estimate compressed file size
     *
     * KR: 원본 파일을 압축했을 때 예상 크기 계산 (대략적)
     * EN: Calculate estimated size when compressing original file (approximate)
     *
     * @param originalSize 원본 파일 크기 (바이트) / Original file size (bytes)
     * @return 예상 압축 크기 (바이트) / Estimated compressed size (bytes)
     */
    public static long estimateCompressedSize(long originalSize) {
        // XML 파일은 보통 10:1 ~ 20:1 압축 비율 / XML files typically have 10:1 ~ 20:1 compression ratio
        // 안전하게 10:1 비율 사용 / Use 10:1 ratio for safety
        return originalSize / 10;
    }

    /**
     * 압축 해제된 파일 크기 추정 / Estimate decompressed file size
     *
     * KR: 압축 파일의 원본 크기 추정 (대략적)
     * EN: Estimate original size of compressed file (approximate)
     *
     * @param compressedSize 압축 파일 크기 (바이트) / Compressed file size (bytes)
     * @return 예상 원본 크기 (바이트) / Estimated original size (bytes)
     */
    public static long estimateDecompressedSize(long compressedSize) {
        // 압축 비율 10:1 역산 / Reverse 10:1 compression ratio
        return compressedSize * 10;
    }

    /**
     * 파일명에서 .gz 확장자 제거 / Remove .gz extension from filename
     *
     * KR: "file.xml.gz" → "file.xml" 변환
     * EN: Convert "file.xml.gz" → "file.xml"
     *
     * @param gzipPath GZip 파일 경로 / GZip file path
     * @return 압축 해제 파일명 / Decompressed filename
     */
    public static Path removeGzipExtension(Path gzipPath) {
        if (gzipPath == null) {
            return null;
        }

        String fileName = gzipPath.getFileName().toString();
        if (fileName.toLowerCase().endsWith(".gz")) {
            String newFileName = fileName.substring(0, fileName.length() - 3);
            return gzipPath.getParent() != null
                    ? gzipPath.getParent().resolve(newFileName)
                    : Path.of(newFileName);
        }

        return gzipPath;
    }

    /**
     * 파일명에 .gz 확장자 추가 / Add .gz extension to filename
     *
     * KR: "file.xml" → "file.xml.gz" 변환
     * EN: Convert "file.xml" → "file.xml.gz"
     *
     * @param filePath 파일 경로 / File path
     * @return GZip 파일명 / GZip filename
     */
    public static Path addGzipExtension(Path filePath) {
        if (filePath == null) {
            return null;
        }

        String fileName = filePath.getFileName().toString();
        if (!fileName.toLowerCase().endsWith(".gz")) {
            String newFileName = fileName + ".gz";
            return filePath.getParent() != null
                    ? filePath.getParent().resolve(newFileName)
                    : Path.of(newFileName);
        }

        return filePath;
    }

    /**
     * Reader 열기 (GZip 자동 처리) / Open Reader (auto-handle GZip)
     *
     * KR: GZip 파일을 자동으로 처리하는 BufferedReader 반환
     * EN: Return BufferedReader that auto-handles GZip files
     *
     * @param filePath 파일 경로 / File path
     * @return BufferedReader
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public static BufferedReader openReader(Path filePath) throws IOException {
        InputStream inputStream = openInputStream(filePath);
        return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    }

    /**
     * Writer 열기 (GZip 압축) / Open Writer (GZip compression)
     *
     * KR: GZip 압축하는 BufferedWriter 반환
     * EN: Return BufferedWriter that compresses to GZip
     *
     * @param filePath 파일 경로 / File path
     * @return BufferedWriter
     * @throws IOException 파일 쓰기 오류 / File writing error
     */
    public static BufferedWriter openGzipWriter(Path filePath) throws IOException {
        OutputStream outputStream = openGzipOutputStream(filePath);
        return new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
    }
}
