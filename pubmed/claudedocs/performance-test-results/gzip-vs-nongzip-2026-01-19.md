# PubMed Performance Test Report

**테스트:** gzip-vs-nongzip
**실행 일시:** 2026-01-19 13:36:08

---

## 성능 측정 결과 / Performance Results

| 테스트 | 논문 수 | 처리 시간 | 처리 속도 | 메모리 사용 | 파일 크기 |
|--------|---------|-----------|-----------|-------------|----------|
| GZip Compressed | 30,000 | 1.26s | 23,753/s | 300.1MB | 18.8MB |
| Uncompressed | 30,000 | 0.97s | 30,801/s | 300.1MB | 186.5MB |

## 상세 메트릭 / Detailed Metrics

### GZip Compressed

```
테스트: GZip Compressed
  논문 수: 30,000
  처리 시간: 1.263s
  처리 속도: 23,753 articles/sec
  메모리 사용: 300.1MB
  평균 메모리/article: 10.24KB
```

### Uncompressed

```
테스트: Uncompressed
  논문 수: 30,000
  처리 시간: 0.974s
  처리 속도: 30,801 articles/sec
  메모리 사용: 300.1MB
  평균 메모리/article: 10.24KB
```

---

**생성 시각:** 2026-01-19 13:36:08
