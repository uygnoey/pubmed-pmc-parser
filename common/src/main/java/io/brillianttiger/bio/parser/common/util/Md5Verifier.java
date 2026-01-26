package io.brillianttiger.bio.parser.common.util;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.regex.*;

/**
 * Md5Verifier / PubMed 파일 무결성 검증 유틸리티
 *
 * KR: PubMed 파일 무결성 검증 유틸리티
 *     PubMed FTP는 각 XML 파일에 대해 .md5 체크섬 파일을 제공합니다.
 * EN: PubMed file integrity verification utility
 *     PubMed FTP provides .md5 checksum files for each XML file.
 *
 * MD5 파일 형식 / MD5 file format:
 *   MD5(pubmed25n0001.xml.gz)= d41d8cd98f00b204e9800998ecf8427e
 */
public final class Md5Verifier {

    private static final Pattern MD5_PATTERN = Pattern.compile("([0-9a-fA-F]{32})");

    private Md5Verifier() {
        // Utility class - prevent instantiation
    }

    /**
     * 파일의 MD5 해시 계산 / Calculate MD5 hash of file
     *
     * @param file file path
     * @return MD5 hash in hexadecimal
     * @throws IOException if file read error occurs
     * @throws NoSuchAlgorithmException if MD5 algorithm not available
     */
    public static String calculateMd5(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new BufferedInputStream(Files.newInputStream(file), 65536)) {
            byte[] buffer = new byte[65536];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        return bytesToHex(md.digest());
    }

    /**
     * .md5 파일에서 해시값 추출 / Extract hash value from .md5 file
     *
     * @param md5File MD5 file path
     * @return MD5 hash value
     * @throws IOException if file read error or invalid format
     */
    public static String extractMd5FromFile(Path md5File) throws IOException {
        String content = Files.readString(md5File).trim();
        Matcher matcher = MD5_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).toLowerCase();
        }
        throw new IOException("Invalid MD5 file format: " + md5File);
    }

    /**
     * 파일 무결성 검증 / Verify file integrity
     *
     * @param dataFile data file path
     * @param md5File MD5 checksum file path
     * @return true if MD5 matches, false otherwise
     * @throws IOException if file read error occurs
     * @throws NoSuchAlgorithmException if MD5 algorithm not available
     */
    public static boolean verify(Path dataFile, Path md5File)
            throws IOException, NoSuchAlgorithmException {
        String expected = extractMd5FromFile(md5File).toLowerCase();
        String actual = calculateMd5(dataFile).toLowerCase();
        return expected.equals(actual);
    }

    /**
     * PubMed 파일 검증 (자동으로 .md5 파일 찾기) / Verify PubMed file (auto-find .md5 file)
     *
     * @param xmlGzFile PubMed XML.gz file path
     * @return true if verification succeeds, false otherwise
     * @throws IOException if file read error or MD5 file not found
     * @throws NoSuchAlgorithmException if MD5 algorithm not available
     */
    public static boolean verifyPubmedFile(Path xmlGzFile)
            throws IOException, NoSuchAlgorithmException {
        Path md5File = Path.of(xmlGzFile.toString() + ".md5");
        if (!Files.exists(md5File)) {
            throw new FileNotFoundException("MD5 file not found: " + md5File);
        }
        return verify(xmlGzFile, md5File);
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환 / Convert byte array to hexadecimal string
     *
     * @param bytes byte array
     * @return hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
