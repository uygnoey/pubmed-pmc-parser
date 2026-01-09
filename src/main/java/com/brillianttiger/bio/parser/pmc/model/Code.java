package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Code / 코드
 *
 * KR: 프로그램 코드 또는 스크립트 블록. JATS 1.4 완전 준수 모델.
 * EN: Program code or script block. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT code (#PCDATA %code-elements;)*>
 *
 *      <!ATTLIST code
 *          %jats-common-atts;
 *          code-type CDATA #IMPLIED
 *          code-version CDATA #IMPLIED
 *          executable (yes | no) "no"
 *          language CDATA #IMPLIED
 *          language-version CDATA #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          platforms CDATA #IMPLIED
 *          position (anchor | background | float | margin) #IMPLIED
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/code.html
 *
 * Note: The code element is used to preserve program code, scripts, algorithms,
 * or other computer-readable text. It typically preserves whitespace, line breaks,
 * and formatting. The language attribute specifies the programming language.
 *
 * Example:
 * <code language="python">
 * def calculate_mean(values):
 *     return sum(values) / len(values)
 * </code>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 코드 블록의 고유 식별자.
     * EN: Unique identifier for this code block.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 코드 타입 / Code type
     *
     * KR: 코드의 유형 (program, script, algorithm 등).
     * EN: Type of code (program, script, algorithm, etc.).
     *
     * DTD: code-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "program", "script", "algorithm", "function", "class"
     */
    private String codeType;

    /**
     * 코드 버전 / Code version
     *
     * KR: 코드의 버전 정보.
     * EN: Version information for the code.
     *
     * DTD: code-version CDATA #IMPLIED
     * Required: NO
     *
     * Example: "1.0", "2.5.1"
     */
    private String codeVersion;

    /**
     * 실행 가능 여부 / Executable
     *
     * KR: 코드가 실행 가능한지 여부.
     * EN: Whether the code is executable.
     *
     * DTD: executable (yes | no) "no"
     * Required: NO (default: "no")
     *
     * Example: "yes", "no"
     */
    private String executable;

    /**
     * 프로그래밍 언어 / Programming language
     *
     * KR: 코드의 프로그래밍 언어.
     * EN: Programming language of the code.
     *
     * DTD: language CDATA #IMPLIED
     * Required: NO
     *
     * Example: "python", "java", "javascript", "r", "c", "c++", "bash", "sql"
     */
    private String language;

    /**
     * 언어 버전 / Language version
     *
     * KR: 프로그래밍 언어의 버전.
     * EN: Version of the programming language.
     *
     * DTD: language-version CDATA #IMPLIED
     * Required: NO
     *
     * Example: "3.9", "17", "ES6"
     */
    private String languageVersion;

    /**
     * 방향 / Orientation
     *
     * KR: 코드 블록의 페이지 방향.
     * EN: Page orientation for the code block.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     *
     * Example: "portrait", "landscape"
     */
    private String orientation;

    /**
     * 플랫폼 / Platforms
     *
     * KR: 코드가 실행되는 플랫폼 목록.
     * EN: List of platforms where the code runs.
     *
     * DTD: platforms CDATA #IMPLIED
     * Required: NO
     *
     * Example: "Windows Linux macOS", "Android iOS"
     */
    private String platforms;

    /**
     * 위치 / Position
     *
     * KR: 코드 블록의 배치 위치.
     * EN: Positioning of the code block.
     *
     * DTD: position (anchor | background | float | margin) #IMPLIED
     * Required: NO
     *
     * Example: "anchor", "background", "float", "margin"
     */
    private String position;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 코드 블록의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this code block.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 코드 텍스트 / Code text
     *
     * KR: 프로그램 코드의 텍스트 콘텐츠.
     * EN: Text content of the program code.
     *
     * DTD: #PCDATA
     * Required: NO (can be empty)
     *
     * Note: This field contains the actual code text. Whitespace and line breaks
     * should be preserved. For syntax highlighting and proper display, the
     * language attribute should be set appropriately.
     *
     * Example:
     * def calculate_mean(values):
     *     return sum(values) / len(values)
     */
    private String value;
}
