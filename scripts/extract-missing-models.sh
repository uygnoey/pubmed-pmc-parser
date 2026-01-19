#!/bin/bash
# 누락된 모델 목록 추출
grep -A 200 "## ❌ 누락된 모델" claudedocs/jats-analysis/comparison-report.md | \
    grep -E '^\- `[A-Z]' | \
    sed 's/^- `\([^`]*\)`.*/\1/' | \
    sort > claudedocs/jats-analysis/missing-models-list.txt

echo "누락된 모델 목록 추출 완료: $(wc -l < claudedocs/jats-analysis/missing-models-list.txt) 개"
