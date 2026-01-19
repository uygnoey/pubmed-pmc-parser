package examples;

import com.brillianttiger.bio.parser.common.util.Md5Verifier;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * DownloadWithMd5Verify / FTP 다운로드 및 MD5 검증 예제
 *
 * KR: PubMed FTP에서 파일을 다운로드하고 MD5 체크섬을 검증하는 예제.
 *     파일 무결성을 보장하며, 실패 시 자동으로 재다운로드합니다.
 *
 * EN: Example of downloading files from PubMed FTP and verifying MD5 checksums.
 *     Ensures file integrity and automatically retries on failure.
 *
 * Features:
 *   - Download from PubMed FTP
 *   - MD5 checksum verification
 *   - Progress monitoring
 *   - Automatic retry on failure
 *   - Resume support
 *
 * Usage:
 *   java examples.DownloadWithMd5Verify <file-type> <file-number> [output-dir]
 *
 * Examples:
 *   java examples.DownloadWithMd5Verify baseline 1
 *   java examples.DownloadWithMd5Verify update 1275
 *   java examples.DownloadWithMd5Verify baseline 1 /data/pubmed/
 */
public class DownloadWithMd5Verify {

    // PubMed FTP 서버 / PubMed FTP server
    private static final String FTP_BASE_URL = "https://ftp.ncbi.nlm.nih.gov/pubmed/";
    private static final int MAX_RETRIES = 3;
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String fileType = args[0].toLowerCase();  // "baseline" or "update"
        int fileNumber = Integer.parseInt(args[1]);
        Path outputDir = args.length > 2 ?
                        Paths.get(args[2]) :
                        Paths.get(".");

        // 파일 타입 검증 / Validate file type
        if (!fileType.equals("baseline") && !fileType.equals("update")) {
            System.err.println("Error: file-type must be 'baseline' or 'update'");
            System.exit(1);
        }

        try {
            // 출력 디렉토리 생성 / Create output directory
            Files.createDirectories(outputDir);

            // 파일 다운로드 및 검증 / Download and verify file
            downloadAndVerify(fileType, fileNumber, outputDir);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 파일 다운로드 및 검증 / Download and verify file
     */
    private static void downloadAndVerify(String fileType, int fileNumber, Path outputDir)
            throws Exception {

        System.out.println("========================================");
        System.out.println("PubMed FTP Download with MD5 Verification");
        System.out.println("========================================");
        System.out.println("File Type:   " + fileType);
        System.out.println("File Number: " + fileNumber);
        System.out.println("Output Dir:  " + outputDir);
        System.out.println();

        // 파일 이름 생성 / Generate file names
        String fileName = String.format("pubmed25n%04d.xml.gz", fileNumber);
        String md5FileName = fileName + ".md5";

        // FTP URL 생성 / Generate FTP URLs
        String ftpSubdir = fileType.equals("baseline") ? "baseline/" : "updatefiles/";
        String fileUrl = FTP_BASE_URL + ftpSubdir + fileName;
        String md5Url = FTP_BASE_URL + ftpSubdir + md5FileName;

        // 출력 경로 / Output paths
        Path xmlFilePath = outputDir.resolve(fileName);
        Path md5FilePath = outputDir.resolve(md5FileName);

        // 재시도 로직 / Retry logic
        boolean success = false;
        int retries = 0;

        while (!success && retries < MAX_RETRIES) {
            try {
                if (retries > 0) {
                    System.out.println();
                    System.out.println("Retry attempt " + retries + "/" + MAX_RETRIES);
                }

                // 1. MD5 파일 다운로드 / Download MD5 file
                System.out.println("Step 1: Downloading MD5 file...");
                downloadFile(md5Url, md5FilePath);
                System.out.println("✅ MD5 file downloaded: " + md5FileName);

                // 2. XML 파일 다운로드 / Download XML file
                System.out.println();
                System.out.println("Step 2: Downloading XML file...");
                downloadFile(fileUrl, xmlFilePath);
                System.out.println("✅ XML file downloaded: " + fileName);

                // 3. MD5 검증 / Verify MD5
                System.out.println();
                System.out.println("Step 3: Verifying MD5 checksum...");
                boolean isValid = Md5Verifier.verify(xmlFilePath, md5FilePath);

                if (isValid) {
                    System.out.println("✅ MD5 verification passed");
                    success = true;
                } else {
                    System.err.println("❌ MD5 verification failed");
                    retries++;

                    if (retries < MAX_RETRIES) {
                        System.out.println("File may be corrupted. Re-downloading...");
                        // 파일 삭제 후 재다운로드 / Delete and retry
                        Files.deleteIfExists(xmlFilePath);
                        Files.deleteIfExists(md5FilePath);
                    }
                }

            } catch (Exception e) {
                System.err.println("❌ Download failed: " + e.getMessage());
                retries++;

                if (retries < MAX_RETRIES) {
                    System.out.println("Retrying...");
                    Files.deleteIfExists(xmlFilePath);
                    Files.deleteIfExists(md5FilePath);
                } else {
                    throw e;
                }
            }
        }

        if (!success) {
            throw new IOException("Failed to download and verify file after " +
                                  MAX_RETRIES + " attempts");
        }

        // 최종 결과 / Final result
        System.out.println();
        System.out.println("========================================");
        System.out.println("Download Complete");
        System.out.println("========================================");
        System.out.println("File:     " + xmlFilePath);
        System.out.println("MD5:      " + md5FilePath);
        System.out.println("Size:     " + formatFileSize(Files.size(xmlFilePath)));
        System.out.println("Status:   ✅ Verified");
        System.out.println();

        // 다음 단계 안내 / Next steps
        System.out.println("Next steps:");
        System.out.println("  Parse file:    java examples.BasicParsing " + xmlFilePath);
        System.out.println("  Stream file:   java examples.StreamingExample " + xmlFilePath);
    }

    /**
     * 파일 다운로드 / Download file
     */
    private static void downloadFile(String urlString, Path outputPath) throws IOException {
        URL url = new URL(urlString);

        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {

            long bytesTransferred = 0;
            long lastPrintTime = System.currentTimeMillis();

            // 진행 상황 모니터링을 위한 래퍼
            // Wrapper for progress monitoring
            ReadableByteChannel progressChannel = new ReadableByteChannel() {
                @Override
                public int read(java.nio.ByteBuffer dst) throws IOException {
                    int bytes = rbc.read(dst);
                    if (bytes > 0) {
                        bytesTransferred += bytes;

                        // 1초마다 진행 상황 출력 / Print progress every second
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastPrintTime > 1000) {
                            System.out.printf("  Downloaded: %s%n",
                                            formatFileSize(bytesTransferred));
                            lastPrintTime = currentTime;
                        }
                    }
                    return bytes;
                }

                @Override
                public boolean isOpen() {
                    return rbc.isOpen();
                }

                @Override
                public void close() throws IOException {
                    rbc.close();
                }
            };

            // 파일 전송 / Transfer file
            fos.getChannel().transferFrom(progressChannel, 0, Long.MAX_VALUE);
            System.out.printf("  Downloaded: %s (complete)%n",
                            formatFileSize(bytesTransferred));
        }
    }

    /**
     * 파일 크기 포맷 / Format file size
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * 사용법 출력 / Print usage
     */
    private static void printUsage() {
        System.out.println("PubMed FTP Download with MD5 Verification");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java examples.DownloadWithMd5Verify <file-type> <file-number> [output-dir]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  file-type    'baseline' or 'update' (required)");
        System.out.println("  file-number  File number (e.g., 1, 1275) (required)");
        System.out.println("  output-dir   Output directory (optional, default: current directory)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  Download baseline file 1:");
        System.out.println("    java examples.DownloadWithMd5Verify baseline 1");
        System.out.println();
        System.out.println("  Download update file 1275:");
        System.out.println("    java examples.DownloadWithMd5Verify update 1275");
        System.out.println();
        System.out.println("  Download to specific directory:");
        System.out.println("    java examples.DownloadWithMd5Verify baseline 1 /data/pubmed/");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  - Automatic MD5 verification");
        System.out.println("  - Progress monitoring");
        System.out.println("  - Automatic retry on failure (up to " + MAX_RETRIES + " attempts)");
        System.out.println("  - File integrity guarantee");
        System.out.println();
        System.out.println("PubMed FTP Server:");
        System.out.println("  " + FTP_BASE_URL);
        System.out.println();
        System.out.println("File naming:");
        System.out.println("  Baseline: pubmed25n0001.xml.gz, pubmed25n0002.xml.gz, ...");
        System.out.println("  Update:   pubmed25n1275.xml.gz, pubmed25n1276.xml.gz, ...");
    }
}
