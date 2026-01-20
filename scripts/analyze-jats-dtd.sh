#!/bin/bash
# JATS DTD 분석 스크립트 / JATS DTD Analysis Script
# DTD에서 모든 element와 attribute 정의를 추출합니다

set -euo pipefail

DTD_FILE="${1:-claudedocs/jats-dtd/JATS-archivearticle1-4.dtd}"
OUTPUT_DIR="claudedocs/jats-analysis"

mkdir -p "$OUTPUT_DIR"

echo "=== JATS DTD 분석 시작 ==="
echo "DTD 파일: $DTD_FILE"

# 1. 모든 ELEMENT 정의 추출
echo "1. Element 정의 추출 중..."
grep -E '<!ELEMENT' "$DTD_FILE" | \
    sed 's/<!ELEMENT[[:space:]]*\([a-zA-Z0-9_-]*\).*/\1/' | \
    sort -u > "$OUTPUT_DIR/elements.txt"

ELEMENT_COUNT=$(wc -l < "$OUTPUT_DIR/elements.txt" | tr -d ' ')
echo "   발견된 Element 개수: $ELEMENT_COUNT"

# 2. 모든 ATTLIST 정의 추출
echo "2. Attribute 정의 추출 중..."
grep -E '<!ATTLIST' "$DTD_FILE" | \
    sed 's/<!ATTLIST[[:space:]]*\([a-zA-Z0-9_-]*\).*/\1/' | \
    sort -u > "$OUTPUT_DIR/elements-with-attributes.txt"

ATTLIST_COUNT=$(wc -l < "$OUTPUT_DIR/elements-with-attributes.txt" | tr -d ' ')
echo "   Attribute를 가진 Element 개수: $ATTLIST_COUNT"

# 3. Element별 상세 정의 추출
echo "3. Element별 상세 정의 추출 중..."
grep -A 1 '<!ELEMENT' "$DTD_FILE" > "$OUTPUT_DIR/element-definitions.txt"

# 4. Attribute별 상세 정의 추출
echo "4. Attribute별 상세 정의 추출 중..."
grep -A 5 '<!ATTLIST' "$DTD_FILE" > "$OUTPUT_DIR/attribute-definitions.txt"

# 5. 현재 Java 모델 클래스 목록 추출
echo "5. 현재 Java 모델 클래스 목록 추출 중..."
if [ -d "src/main/java/com/brillianttiger/bio/parser/pmc/model" ]; then
    find src/main/java/com/brillianttiger/bio/parser/pmc/model -name "*.java" -type f | \
        sed 's/.*\/\(.*\)\.java/\1/' | \
        sort > "$OUTPUT_DIR/current-java-models.txt"

    MODEL_COUNT=$(wc -l < "$OUTPUT_DIR/current-java-models.txt" | tr -d ' ')
    echo "   현재 Java 모델 개수: $MODEL_COUNT"
fi

# 6. Element 이름을 Java 클래스 이름으로 변환 (kebab-case to PascalCase)
echo "6. DTD Element를 Java 클래스명으로 변환 중..."
cat "$OUTPUT_DIR/elements.txt" | while read element; do
    # kebab-case를 PascalCase로 변환
    # 예: article-meta -> ArticleMeta
    echo "$element" | \
        sed 's/-\([a-z]\)/\U\1/g' | \
        sed 's/^\([a-z]\)/\U\1/'
done | sort -u > "$OUTPUT_DIR/expected-java-models.txt"

EXPECTED_COUNT=$(wc -l < "$OUTPUT_DIR/expected-java-models.txt" | tr -d ' ')
echo "   예상 Java 모델 개수: $EXPECTED_COUNT"

# 7. 누락된 모델 찾기
if [ -f "$OUTPUT_DIR/current-java-models.txt" ]; then
    echo "7. 누락된 모델 찾기..."
    comm -23 "$OUTPUT_DIR/expected-java-models.txt" "$OUTPUT_DIR/current-java-models.txt" \
        > "$OUTPUT_DIR/missing-models.txt"

    MISSING_COUNT=$(wc -l < "$OUTPUT_DIR/missing-models.txt" | tr -d ' ')
    echo "   누락된 모델 개수: $MISSING_COUNT"

    if [ $MISSING_COUNT -gt 0 ]; then
        echo ""
        echo "=== 누락된 모델 (처음 20개) ==="
        head -20 "$OUTPUT_DIR/missing-models.txt"
    fi
fi

# 8. 추가로 구현된 모델 찾기 (DTD에 없는 것)
if [ -f "$OUTPUT_DIR/current-java-models.txt" ]; then
    echo ""
    echo "8. DTD에 없는 추가 모델 찾기..."
    comm -13 "$OUTPUT_DIR/expected-java-models.txt" "$OUTPUT_DIR/current-java-models.txt" \
        > "$OUTPUT_DIR/extra-models.txt"

    EXTRA_COUNT=$(wc -l < "$OUTPUT_DIR/extra-models.txt" | tr -d ' ')
    echo "   추가 모델 개수: $EXTRA_COUNT"

    if [ $EXTRA_COUNT -gt 0 ]; then
        echo ""
        echo "=== 추가 모델 (처음 20개) ==="
        head -20 "$OUTPUT_DIR/extra-models.txt"
    fi
fi

echo ""
echo "=== 분석 완료 ==="
echo "결과 디렉토리: $OUTPUT_DIR"
echo ""
echo "생성된 파일:"
echo "  - elements.txt: DTD의 모든 element 목록"
echo "  - elements-with-attributes.txt: Attribute를 가진 element 목록"
echo "  - element-definitions.txt: Element 정의 상세"
echo "  - attribute-definitions.txt: Attribute 정의 상세"
echo "  - expected-java-models.txt: DTD 기반 예상 Java 클래스 목록"
echo "  - current-java-models.txt: 현재 구현된 Java 클래스 목록"
echo "  - missing-models.txt: 누락된 모델 목록"
echo "  - extra-models.txt: DTD에 없는 추가 모델 목록"
