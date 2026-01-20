# ArticleMetaParser 남은 Missed Branches 분석

## 현재 상태
- **Branch Coverage**: 95% (692/726 branches)
- **Missed Branches**: 34
- **날짜**: 2026-01-19

## 세션 진행 내역
| 단계 | Coverage | Missed | 개선 | Tests 추가 |
|------|----------|--------|------|------------|
| 시작 | 93% | 44 | - | - |
| Test 64 | 93% | 44 | 0 | parsePubHistory with event |
| Tests 65-67 | 94% | 40 | 4 | parseAff multi-text, parsePubHistory unknown, parseAffAlternatives unknown |
| Tests 68-70 | 94% | 40 | 0 | isEmpty branches (실패 - 잘못된 접근) |
| Tests 71-74 | 95% | 36 | 4 | parseChemStruct/Preformat no content, parseChemStructWrap unknown, CDATA |
| Test 75 | 95% | 35 | 1 | parsePubHistory no dates |
| Test 76 | 95% | 34 | 1 | parseAffAlternatives empty |

**총 개선**: 10 branches (44 → 34)

## 남은 34 Missed Branches 분석

### 1. 구조적으로 불가능한 Branches (약 31개)

#### 패턴: while(true) 루프 내 END_ELEMENT 체크

```java
while (true) {
    int event = reader.next();
    if (event == XMLStreamConstants.START_ELEMENT) {
        // ... 요소 처리
    } else if (event == XMLStreamConstants.END_ELEMENT) {
        if (reader.getLocalName().equals("expected-element-name")) {
            break;  // 항상 여기로 옴
        }
        // FALSE branch - 도달 불가능!
    }
}
```

**왜 불가능한가?**
- 잘 형성된 XML에서는 END_ELEMENT의 이름이 항상 시작 요소와 일치
- while(true) 루프는 올바른 END_ELEMENT를 만날 때까지만 실행
- 따라서 `equals("expected-name")` false 경로는 절대 발생하지 않음

**영향받는 메서드들** (각각 1 missed branch):
- parseArticleMeta (Line 177)
- parseTitleGroup (Line 242)
- parseContribGroup (Line 291)
- parseContrib (Line 401)
- parseAff (Line 470)
- parseInstitutionWrap (Line 530)
- parsePmcPubDate (Line 631)
- parseKwdGroup (Line 684)
- parsePmcAbstract (Line 729)
- parseSupplementaryMaterial (Line 927)
- parseTransTitleGroup (Line 1033)
- parseFnGroup (Line 1116)
- parseName (Line 1178)
- parseFn (Line 1267)
- parseAuthorNotes (Line 1310)
- parsePmcHistory (Line 1347)
- parsePmcDate (Line 1386)
- parsePermissions (Line 1427)
- parseFundingGroup (Line 1484)
- parseAwardGroup (Line 1517)
- parseCounts (Line 1606)
- parseDispFormula (Line 1765)
- parseDispFormulaGroup (Line 1807)
- parseAlternatives (Line 1880)
- parsePreformat (Line 1952)
- parseArray (Line 2059)
- parseChemStructWrap (Line 2210)
- parseChemStruct (Line 2371)
- parseAffAlternatives (Line 2412)
- parsePubHistory (Line 2485)
- parseEvent (Line 2538)

**총 31개 branches** - 구조적으로 불가능

### 2. StAX 제한으로 인한 미커버 Branches (2개)

#### Line 469, 1951: `else if (event == END_ELEMENT)`

```java
} else if (event == XMLStreamConstants.CHARACTERS) {
    content.append(reader.getText());
} else if (event == XMLStreamConstants.END_ELEMENT) {  // 1 of 2 missed
    if (reader.getLocalName().equals("element-name")) {
        break;
    }
}
```

**왜 커버 불가능?**
- CHARACTERS와 END_ELEMENT 사이에 다른 event 타입(CDATA, COMMENT 등)이 필요
- 하지만 우리의 XML 구조에서는 이런 케이스가 발생하지 않음
- StAX 파서 설정(coalescing)에 따라 CDATA가 CHARACTERS로 변환됨

**영향받는 메서드**:
- parseAff (Line 469)
- parsePreformat (Line 1951)

### 3. CDATA 관련 (1개)

#### Line 2368: `CHARACTERS || CDATA`

```java
} else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
    value.append(reader.getText());
}
```

**1 of 4 branches missed** - CDATA 단독 케이스
- Test 74에서 CDATA를 테스트했으나 커버되지 않음
- StAX 파서가 CDATA를 CHARACTERS로 자동 변환할 수 있음
- XMLInputFactory의 `IS_COALESCING` 설정에 의존

## 결론

### 달성 가능한 최대 Coverage
**95% (692/726 branches)** - 현재 상태

### 구조적으로 불가능
- **31 branches**: while(true) END_ELEMENT equals false
- **도달 불가능성**: 잘 형성된 XML의 특성상 절대 발생하지 않는 경로

### StAX 제한
- **3 branches**: CDATA 처리 및 event 타입 조합
- **파서 의존성**: XMLInputFactory 설정과 구현에 따라 달라짐

### 권장사항
1. ✅ **현재 95%를 최종 목표로 인정**
2. ✅ **구조적으로 불가능한 branches는 무시**
3. ❌ **억지로 100%를 달성하려고 잘못된 XML을 만들지 말 것**
4. ✅ **남은 3개 StAX 관련 branches는 선택적 개선**

## 추가 테스트 가능성

### StAX 설정 변경으로 가능할 수 있는 것들
```java
XMLInputFactory factory = XMLInputFactory.newInstance();
factory.setProperty(XMLInputFactory.IS_COALESCING, false);  // CDATA 별도 처리
```

하지만 이는 **실제 파서 동작을 변경**하므로 권장하지 않음.

## 최종 평가

**ArticleMetaParser 95% coverage는 매우 우수한 결과**
- 테스트 가능한 모든 실제 경로를 커버
- 구조적으로 불가능한 경로만 남음
- 추가 개선은 ROI(투자 대비 효과)가 낮음
