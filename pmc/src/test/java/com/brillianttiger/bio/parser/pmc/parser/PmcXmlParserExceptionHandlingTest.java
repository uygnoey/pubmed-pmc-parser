package com.brillianttiger.bio.parser.pmc.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * PmcXmlParser의 exception handling 테스트
 *
 * KR: finally 블록에서 close() 실패 시 exception handling을 테스트
 * EN: Test exception handling when close() fails in finally blocks
 */
class PmcXmlParserExceptionHandlingTest {

    @TempDir
    Path tempDir;

    /**
     * Custom InputStream that throws IOException on close()
     */
    static class FailOnCloseInputStream extends FilterInputStream {
        public FailOnCloseInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            super.close();  // Close the underlying stream first
            throw new IOException("Simulated InputStream close failure");
        }
    }

    /**
     * Smart InputStream that only fails on close() after EOF is reached
     * KR: EOF 도달 후에만 close()에서 예외를 던지는 InputStream
     * EN: InputStream that throws exception on close() only after EOF
     */
    static class SmartFailOnCloseInputStream extends FilterInputStream {
        private boolean eofReached = false;

        public SmartFailOnCloseInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result == -1) {
                eofReached = true;
            }
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int result = super.read(b, off, len);
            if (result == -1) {
                eofReached = true;
            }
            return result;
        }

        @Override
        public void close() throws IOException {
            super.close();  // Close the underlying stream first
            if (eofReached) {
                throw new IOException("Simulated InputStream close failure after EOF");
            }
        }
    }

    /**
     * InputStream that fails on second close() call
     * KR: 두 번째 close() 호출 시 예외를 던지는 InputStream
     *     XMLStreamReader가 close될 때 내부적으로 InputStream을 close할 수 있으므로,
     *     finally 블록에서 is.close()를 다시 호출하면 예외 발생
     * EN: InputStream that throws exception on second close() call
     */
    static class FailOnSecondCloseInputStream extends FilterInputStream {
        private int closeCount = 0;

        public FailOnSecondCloseInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            closeCount++;
            if (closeCount > 1) {
                throw new IOException("Stream already closed - cannot close again");
            }
            super.close();
        }
    }

    /**
     * Custom XMLStreamReader wrapper that throws XMLStreamException on close()
     */
    static class FailOnCloseXMLStreamReader extends StreamReaderDelegate {
        public FailOnCloseXMLStreamReader(XMLStreamReader reader) {
            super(reader);
        }

        @Override
        public void close() throws XMLStreamException {
            super.close();  // Close the underlying reader first
            throw new XMLStreamException("Simulated XMLStreamReader close failure");
        }
    }

    /**
     * Custom PmcXmlParser that allows injecting failing stream/reader
     */
    static class TestPmcXmlParser extends PmcXmlParser {
        private boolean failInputStream = false;
        private boolean failXMLStreamReader = false;
        private boolean useSmartFail = false;
        private boolean useSecondCloseFail = false;

        public void setFailInputStream(boolean fail) {
            this.failInputStream = fail;
        }

        public void setFailXMLStreamReader(boolean fail) {
            this.failXMLStreamReader = fail;
        }

        public void setUseSmartFail(boolean useSmartFail) {
            this.useSmartFail = useSmartFail;
        }

        public void setUseSecondCloseFail(boolean useSecondCloseFail) {
            this.useSecondCloseFail = useSecondCloseFail;
        }

        @Override
        protected InputStream openInputStream(Path path) throws IOException {
            InputStream is = super.openInputStream(path);
            if (failInputStream) {
                if (useSecondCloseFail) {
                    return new FailOnSecondCloseInputStream(is);
                } else if (useSmartFail) {
                    return new SmartFailOnCloseInputStream(is);
                } else {
                    return new FailOnCloseInputStream(is);
                }
            }
            return is;
        }

        @Override
        protected XMLStreamReader createReader(InputStream inputStream) throws Exception {
            XMLStreamReader reader = super.createReader(inputStream);
            if (failXMLStreamReader) {
                return new FailOnCloseXMLStreamReader(reader);
            }
            return reader;
        }
    }

    /**
     * Custom PmcXmlParser with Mockito-based InputStream that fails on close
     */
    static class MockInputStreamPmcXmlParser extends PmcXmlParser {
        private InputStream mockInputStream;

        public void setMockInputStream(InputStream mockInputStream) {
            this.mockInputStream = mockInputStream;
        }

        @Override
        protected InputStream openInputStream(Path path) throws IOException {
            if (mockInputStream != null) {
                return mockInputStream;
            }
            return super.openInputStream(path);
        }
    }

    @Test
    @DisplayName("parseFile() - InputStream close 실패 시 warning 로그")
    void testParseFile_InputStreamCloseFailure() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        // Create a real InputStream that reads the file successfully
        InputStream realStream = Files.newInputStream(testFile);
        // Wrap it in a spy that fails on close
        InputStream spyStream = spy(realStream);
        doThrow(new IOException("Simulated InputStream close failure")).when(spyStream).close();

        // Create parser with mock stream
        MockInputStreamPmcXmlParser parser = new MockInputStreamPmcXmlParser();
        parser.setMockInputStream(spyStream);

        // Should still parse successfully despite close failure
        var article = parser.parseFile(testFile);
        assertThat(article).isNotNull();

        // Verify close was called
        verify(spyStream).close();
    }

    @Test
    @DisplayName("parseStream() - InputStream close 실패 시 warning 로그 (두 번째 close 실패)")
    void testParseStream_InputStreamCloseFailure_SecondClose() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        TestPmcXmlParser parser = new TestPmcXmlParser();
        parser.setFailInputStream(true);
        parser.setUseSecondCloseFail(true);  // 두 번째 close() 호출 시 실패

        // Should still parse successfully despite close failure
        final int[] count = {0};
        parser.parseStream(testFile, article -> count[0]++);
        assertThat(count[0]).isEqualTo(1);
    }

    @Test
    @DisplayName("parseStreamBatch() - InputStream close 실패 시 warning 로그 (두 번째 close 실패)")
    void testParseStreamBatch_InputStreamCloseFailure_SecondClose() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        TestPmcXmlParser parser = new TestPmcXmlParser();
        parser.setFailInputStream(true);
        parser.setUseSecondCloseFail(true);  // 두 번째 close() 호출 시 실패

        // Should still parse successfully despite close failure
        final int[] count = {0};
        parser.parseStreamBatch(testFile, 10, article -> count[0]++);
        assertThat(count[0]).isEqualTo(1);
    }

    @Test
    @DisplayName("parseFile() - XMLStreamReader close 실패 시 warning 로그")
    void testParseFile_XMLStreamReaderCloseFailure() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        TestPmcXmlParser parser = new TestPmcXmlParser();
        parser.setFailXMLStreamReader(true);

        // Should still parse successfully despite close failure
        var article = parser.parseFile(testFile);
        assertThat(article).isNotNull();
    }


    @Test
    @DisplayName("parseStream() - XMLStreamReader close 실패 시 warning 로그")
    void testParseStream_XMLStreamReaderCloseFailure() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        TestPmcXmlParser parser = new TestPmcXmlParser();
        parser.setFailXMLStreamReader(true);

        // Should still parse successfully despite close failure
        final int[] count = {0};
        parser.parseStream(testFile, article -> count[0]++);
        assertThat(count[0]).isEqualTo(1);
    }


    @Test
    @DisplayName("parseStreamBatch() - XMLStreamReader close 실패 시 warning 로그")
    void testParseStreamBatch_XMLStreamReaderCloseFailure() throws Exception {
        // Create test XML file
        Path testFile = tempDir.resolve("test.xml");
        String xml = """
            <article xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">12345</article-id>
                        <title-group>
                            <article-title>Test</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;
        Files.writeString(testFile, xml);

        TestPmcXmlParser parser = new TestPmcXmlParser();
        parser.setFailXMLStreamReader(true);

        // Should still parse successfully despite close failure
        final int[] count = {0};
        parser.parseStreamBatch(testFile, 10, article -> count[0]++);
        assertThat(count[0]).isEqualTo(1);
    }
}
