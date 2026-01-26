# PubMed Performance Test Report

**테스트:** gzip-vs-nongzip
**실행 일시:** 2026-01-22 12:53:10

---

## 성능 측정 결과 / Performance Results

| 테스트 | 논문 수 | 처리 시간 | 처리 속도 | 메모리 사용 | 파일 크기 |
|--------|---------|-----------|-----------|-------------|----------|
| GZip Compressed | 30,000 | 1.26s | 23,866/s | 299.1MB | 18.8MB |
| Uncompressed | 30,000 | 0.99s | 30,334/s | 299.3MB | 186.5MB |

## 상세 메트릭 / Detailed Metrics

### GZip Compressed

```
테스트: GZip Compressed
  논문 수: 30,000
  처리 시간: 1.257s
  처리 속도: 23,866 articles/sec
  메모리 사용: 299.1MB
  평균 메모리/article: 10.21KB
```

### Uncompressed

```
테스트: Uncompressed
  논문 수: 30,000
  처리 시간: 0.989s
  처리 속도: 30,334 articles/sec
  메모리 사용: 299.3MB
  평균 메모리/article: 10.21KB
```

---

**생성 시각:** 2026-01-22 12:53:10
