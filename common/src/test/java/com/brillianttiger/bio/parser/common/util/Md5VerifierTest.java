package com.brillianttiger.bio.parser.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Md5Verifier 테스트 / Md5Verifier Test
 *
 * KR: MD5 체크섬 검증 유틸리티 테스트
 * EN: Tests for MD5 checksum verification utility
 */
class Md5VerifierTest {

    @TempDir
    Path tempDir;

    @Test
    void testCalculateMd5() throws IOException, NoSuchAlgorithmException {
        // 테스트 파일 생성 / Create test file
        Path testFile = tempDir.resolve("test.txt");
        String content = "Hello, World!";
        Files.writeString(testFile, content);

        // MD5 계산 / Calculate MD5
        String md5 = Md5Verifier.calculateMd5(testFile);

        // 검증 / Verify
        assertNotNull(md5);
        assertEquals(32, md5.length()); // MD5는 32자 / MD5 is 32 chars
        assertTrue(md5.matches("[0-9a-f]{32}")); // 소문자 16진수 / lowercase hex
        assertEquals("65a8e27d8879283831b664bd8b7f0ad4", md5); // "Hello, World!" MD5
    }

    @Test
    void testCalculateMd5EmptyFile() throws IOException, NoSuchAlgorithmException {
        // 빈 파일 / Empty file
        Path emptyFile = tempDir.resolve("empty.txt");
        Files.writeString(emptyFile, "");

        String md5 = Md5Verifier.calculateMd5(emptyFile);

        assertNotNull(md5);
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5); // 빈 파일 MD5 / Empty file MD5
    }

    @Test
    void testCalculateMd5LargeFile() throws IOException, NoSuchAlgorithmException {
        // 대용량 파일 (64KB 이상) / Large file (>64KB)
        Path largeFile = tempDir.resolve("large.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("This is line ").append(i).append("\n");
        }
        Files.writeString(largeFile, sb.toString());

        String md5 = Md5Verifier.calculateMd5(largeFile);

        assertNotNull(md5);
        assertEquals(32, md5.length());
        assertTrue(md5.matches("[0-9a-f]{32}"));
    }

    @Test
    void testExtractMd5FromFile() throws IOException {
        // PubMed MD5 파일 형식 / PubMed MD5 file format
        Path md5File = tempDir.resolve("test.xml.gz.md5");
        String md5Hash = "d41d8cd98f00b204e9800998ecf8427e";
        String content = "MD5(pubmed25n0001.xml.gz)= " + md5Hash;
        Files.writeString(md5File, content);

        String extracted = Md5Verifier.extractMd5FromFile(md5File);

        assertEquals(md5Hash, extracted);
    }

    @Test
    void testExtractMd5FromFileVariousFormats() throws IOException {
        // 다양한 형식 테스트 / Test various formats

        // 형식 1: 공백 포함 / Format 1: with spaces
        Path md5File1 = tempDir.resolve("format1.md5");
        Files.writeString(md5File1, "   MD5(file.xml.gz)= abc123def456789012345678901234ab   ");
        assertEquals("abc123def456789012345678901234ab", Md5Verifier.extractMd5FromFile(md5File1));

        // 형식 2: 해시값만 / Format 2: hash only
        Path md5File2 = tempDir.resolve("format2.md5");
        Files.writeString(md5File2, "abc123def456789012345678901234ab");
        assertEquals("abc123def456789012345678901234ab", Md5Verifier.extractMd5FromFile(md5File2));

        // 형식 3: 대문자 해시 / Format 3: uppercase hash
        Path md5File3 = tempDir.resolve("format3.md5");
        Files.writeString(md5File3, "ABC123DEF456789012345678901234AB");
        assertEquals("abc123def456789012345678901234ab", Md5Verifier.extractMd5FromFile(md5File3));

        // 형식 4: 혼합 대소문자 / Format 4: mixed case
        Path md5File4 = tempDir.resolve("format4.md5");
        Files.writeString(md5File4, "MD5(file.xml.gz)= AbC123DeF456789012345678901234aB");
        assertEquals("abc123def456789012345678901234ab", Md5Verifier.extractMd5FromFile(md5File4));
    }

    @Test
    void testExtractMd5FromFileInvalidFormat() {
        // 잘못된 형식 / Invalid format
        Path md5File = tempDir.resolve("invalid.md5");

        // 형식 1: 너무 짧음 / Format 1: too short
        assertThrows(IOException.class, () -> {
            Files.writeString(md5File, "abc123");
            Md5Verifier.extractMd5FromFile(md5File);
        });

        // 형식 2: 잘못된 문자 / Format 2: invalid characters
        assertThrows(IOException.class, () -> {
            Files.writeString(md5File, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            Md5Verifier.extractMd5FromFile(md5File);
        });

        // 형식 3: 빈 파일 / Format 3: empty file
        assertThrows(IOException.class, () -> {
            Files.writeString(md5File, "");
            Md5Verifier.extractMd5FromFile(md5File);
        });
    }

    @Test
    void testVerify() throws IOException, NoSuchAlgorithmException {
        // 데이터 파일 생성 / Create data file
        Path dataFile = tempDir.resolve("data.txt");
        String content = "Test content for MD5 verification";
        Files.writeString(dataFile, content);

        // MD5 계산 및 저장 / Calculate and save MD5
        String actualMd5 = Md5Verifier.calculateMd5(dataFile);
        Path md5File = tempDir.resolve("data.txt.md5");
        Files.writeString(md5File, "MD5(data.txt)= " + actualMd5);

        // 검증 성공 / Verification success
        assertTrue(Md5Verifier.verify(dataFile, md5File));
    }

    @Test
    void testVerifyFailure() throws IOException, NoSuchAlgorithmException {
        // 데이터 파일 생성 / Create data file
        Path dataFile = tempDir.resolve("data.txt");
        Files.writeString(dataFile, "Original content");

        // 잘못된 MD5 저장 / Save incorrect MD5
        Path md5File = tempDir.resolve("data.txt.md5");
        Files.writeString(md5File, "MD5(data.txt)= ffffffffffffffffffffffffffffffff");

        // 검증 실패 / Verification failure
        assertFalse(Md5Verifier.verify(dataFile, md5File));
    }

    @Test
    void testVerifyPubmedFile() throws IOException, NoSuchAlgorithmException {
        // PubMed XML GZip 파일 시뮬레이션 / Simulate PubMed XML GZip file
        Path xmlGzFile = tempDir.resolve("pubmed25n0001.xml.gz");
        String content = "<?xml version=\"1.0\"?>\n<PubmedArticleSet></PubmedArticleSet>";
        Files.writeString(xmlGzFile, content);

        // MD5 파일 생성 / Create MD5 file
        String actualMd5 = Md5Verifier.calculateMd5(xmlGzFile);
        Path md5File = tempDir.resolve("pubmed25n0001.xml.gz.md5");
        Files.writeString(md5File, "MD5(pubmed25n0001.xml.gz)= " + actualMd5);

        // 자동 검증 / Auto verification
        assertTrue(Md5Verifier.verifyPubmedFile(xmlGzFile));
    }

    @Test
    void testVerifyPubmedFileNotFound() {
        // MD5 파일 없음 / MD5 file not found
        Path xmlGzFile = tempDir.resolve("nonexistent.xml.gz");

        assertThrows(FileNotFoundException.class, () -> {
            Md5Verifier.verifyPubmedFile(xmlGzFile);
        });
    }

    @Test
    void testVerifyPubmedFileWithRealScenario() throws IOException, NoSuchAlgorithmException {
        // 실제 시나리오: baseline 파일 / Real scenario: baseline file
        Path baselineFile = tempDir.resolve("pubmed25n1274.xml.gz");

        // 큰 XML 콘텐츠 시뮬레이션 / Simulate large XML content
        StringBuilder largeXml = new StringBuilder();
        largeXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        largeXml.append("<PubmedArticleSet>\n");
        for (int i = 0; i < 1000; i++) {
            largeXml.append("  <PubmedArticle>\n");
            largeXml.append("    <MedlineCitation Status=\"MEDLINE\" Owner=\"NLM\">\n");
            largeXml.append("      <PMID Version=\"1\">").append(i).append("</PMID>\n");
            largeXml.append("    </MedlineCitation>\n");
            largeXml.append("  </PubmedArticle>\n");
        }
        largeXml.append("</PubmedArticleSet>");

        Files.writeString(baselineFile, largeXml.toString());

        // MD5 파일 생성 / Create MD5 file
        String md5Hash = Md5Verifier.calculateMd5(baselineFile);
        Path md5File = tempDir.resolve("pubmed25n1274.xml.gz.md5");
        Files.writeString(md5File, "MD5(pubmed25n1274.xml.gz)= " + md5Hash);

        // 검증 / Verify
        assertTrue(Md5Verifier.verifyPubmedFile(baselineFile));

        // 파일 변조 시나리오 / File tampering scenario
        Files.writeString(baselineFile, largeXml.toString() + "\n<!-- modified -->");
        assertFalse(Md5Verifier.verifyPubmedFile(baselineFile));
    }

    @Test
    void testBinaryFileVerification() throws IOException, NoSuchAlgorithmException {
        // 바이너리 파일 검증 / Binary file verification
        Path binaryFile = tempDir.resolve("binary.dat");
        byte[] binaryData = new byte[]{0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE};
        Files.write(binaryFile, binaryData);

        // MD5 계산 / Calculate MD5
        String md5 = Md5Verifier.calculateMd5(binaryFile);

        // MD5 파일 생성 / Create MD5 file
        Path md5File = tempDir.resolve("binary.dat.md5");
        Files.writeString(md5File, md5);

        // 검증 / Verify
        assertTrue(Md5Verifier.verify(binaryFile, md5File));
    }

    @Test
    void testCaseInsensitiveVerification() throws IOException, NoSuchAlgorithmException {
        // 대소문자 구분 없이 검증 / Case-insensitive verification
        Path dataFile = tempDir.resolve("case-test.txt");
        Files.writeString(dataFile, "Case Test");

        String md5Lower = Md5Verifier.calculateMd5(dataFile);

        // 대문자 MD5 파일 / Uppercase MD5 file
        Path md5File = tempDir.resolve("case-test.txt.md5");
        Files.writeString(md5File, md5Lower.toUpperCase());

        // 대소문자 무시하고 검증 성공 / Verify succeeds ignoring case
        assertTrue(Md5Verifier.verify(dataFile, md5File));
    }

    @Test
    void testMultipleFileVerification() throws IOException, NoSuchAlgorithmException {
        // 여러 파일 일괄 검증 / Batch verification of multiple files
        for (int i = 1; i <= 5; i++) {
            Path xmlFile = tempDir.resolve("pubmed25n000" + i + ".xml.gz");
            String content = "<?xml version=\"1.0\"?><PubmedArticleSet><Article>" + i + "</Article></PubmedArticleSet>";
            Files.writeString(xmlFile, content);

            // MD5 파일 생성 / Create MD5 file
            String md5 = Md5Verifier.calculateMd5(xmlFile);
            Path md5File = tempDir.resolve("pubmed25n000" + i + ".xml.gz.md5");
            Files.writeString(md5File, "MD5(pubmed25n000" + i + ".xml.gz)= " + md5);

            // 각 파일 검증 / Verify each file
            assertTrue(Md5Verifier.verifyPubmedFile(xmlFile), "File " + i + " verification failed");
        }
    }

    @Test
    void testNonExistentDataFile() {
        // 존재하지 않는 데이터 파일 / Non-existent data file
        Path nonExistent = tempDir.resolve("nonexistent.txt");
        Path md5File = tempDir.resolve("nonexistent.txt.md5");

        assertThrows(IOException.class, () -> {
            Md5Verifier.verify(nonExistent, md5File);
        });
    }

    @Test
    void testNonExistentMd5File() throws IOException {
        // MD5 파일 없음 / MD5 file not found
        Path dataFile = tempDir.resolve("data.txt");
        Files.writeString(dataFile, "Test");
        Path nonExistentMd5 = tempDir.resolve("nonexistent.md5");

        assertThrows(IOException.class, () -> {
            Md5Verifier.verify(dataFile, nonExistentMd5);
        });
    }

    @Test
    void testConsistentMd5Calculation() throws IOException, NoSuchAlgorithmException {
        // 동일한 파일의 MD5는 항상 같아야 함 / MD5 of same file should always be the same
        Path testFile = tempDir.resolve("consistent.txt");
        Files.writeString(testFile, "Consistency Test");

        String md5First = Md5Verifier.calculateMd5(testFile);
        String md5Second = Md5Verifier.calculateMd5(testFile);
        String md5Third = Md5Verifier.calculateMd5(testFile);

        assertEquals(md5First, md5Second);
        assertEquals(md5Second, md5Third);
    }
}
