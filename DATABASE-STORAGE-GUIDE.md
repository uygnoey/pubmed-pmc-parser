# DB 저장 가이드

**생성일:** 2026-01-22 11:35:00
**목적:** 파싱된 PubMed/PMC 데이터를 데이터베이스에 저장하는 방법

---

## 📋 목차
1. [JSON 변환 방법](#1-json-변환-방법)
2. [JPA Entity 매핑](#2-jpa-entity-매핑)
3. [DB 스키마 설계](#3-db-스키마-설계)
4. [실제 저장 코드](#4-실제-저장-코드)
5. [Spring Batch 통합](#5-spring-batch-통합)

---

## 1. JSON 변환 방법

### 1.1 Jackson 라이브러리 추가

**build.gradle에 추가:**
```gradle
dependencies {
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.16.1'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1'
}
```

### 1.2 파싱 결과를 JSON으로 변환

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.brillianttiger.bio.parser.pubmed.PubmedXmlParser;
import io.brillianttiger.bio.parser.pubmed.model.PubmedArticle;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {
    public static void main(String[] args) throws Exception {
        // 1. 파서 생성
        PubmedXmlParser parser = new PubmedXmlParser();

        // 2. 파싱 (스트리밍)
        List<PubmedArticle> articles = new ArrayList<>();
        parser.parseStream(
            Path.of("pubmed25n0001.xml.gz"),
            article -> articles.add(article)
        );

        // 3. JSON 변환
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print

        // 4. JSON 문자열로 변환
        String json = mapper.writeValueAsString(articles);
        System.out.println(json);

        // 5. JSON 파일로 저장
        mapper.writeValue(new File("pubmed_articles.json"), articles);
    }
}
```

### 1.3 JSON 출력 예시

```json
[
  {
    "medlineCitation": {
      "pmid": {
        "version": "1",
        "value": "1"
      },
      "article": {
        "journal": {
          "issn": {
            "issnType": "Print",
            "value": "0006-2944"
          },
          "journalIssue": {
            "volume": "13",
            "issue": "2",
            "pubDate": {
              "year": "1975",
              "month": "Jun"
            }
          },
          "title": {
            "value": "Biochemical medicine"
          }
        },
        "articleTitle": "Formate assay in body fluids: application in methanol poisoning.",
        "authorList": {
          "completeYN": "Y",
          "authors": [
            {
              "lastName": {"value": "Makar"},
              "foreName": {"value": "A B"},
              "initials": {"value": "AB"}
            }
          ]
        }
      }
    }
  }
]
```

---

## 2. JPA Entity 매핑

### 2.1 의존성 추가

**build.gradle:**
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.postgresql:postgresql:42.7.1' // or MySQL
    implementation 'org.hibernate:hibernate-core:6.4.2.Final'
}
```

### 2.2 JPA Entity 클래스 생성

**PubmedArticleEntity.java:**
```java
package io.brillianttiger.bio.parser.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pubmed_articles", indexes = {
    @Index(name = "idx_pmid", columnList = "pmid"),
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_pub_year", columnList = "publication_year")
})
@Data
@NoArgsConstructor
public class PubmedArticleEntity {

    @Id
    @Column(name = "pmid", nullable = false)
    private Long pmid;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "abstract_text", columnDefinition = "TEXT")
    private String abstractText;

    @Column(name = "journal_title")
    private String journalTitle;

    @Column(name = "journal_issn")
    private String journalIssn;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "publication_month")
    private String publicationMonth;

    @Column(name = "publication_day")
    private Integer publicationDay;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorEntity> authors = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeshHeadingEntity> meshHeadings = new ArrayList<>();

    @Column(name = "raw_xml", columnDefinition = "TEXT")
    private String rawXml; // 원본 XML 보관

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

**AuthorEntity.java:**
```java
@Entity
@Table(name = "pubmed_authors")
@Data
@NoArgsConstructor
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pmid", nullable = false)
    private PubmedArticleEntity article;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "fore_name")
    private String foreName;

    @Column(name = "initials")
    private String initials;

    @Column(name = "affiliation", columnDefinition = "TEXT")
    private String affiliation;

    @Column(name = "author_order")
    private Integer authorOrder; // 저자 순서
}
```

**MeshHeadingEntity.java:**
```java
@Entity
@Table(name = "pubmed_mesh_headings", indexes = {
    @Index(name = "idx_mesh_ui", columnList = "descriptor_ui")
})
@Data
@NoArgsConstructor
public class MeshHeadingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pmid", nullable = false)
    private PubmedArticleEntity article;

    @Column(name = "descriptor_name")
    private String descriptorName;

    @Column(name = "descriptor_ui")
    private String descriptorUi;

    @Column(name = "major_topic_yn")
    private Boolean majorTopicYN;

    @ElementCollection
    @CollectionTable(name = "mesh_qualifiers", joinColumns = @JoinColumn(name = "mesh_heading_id"))
    private List<String> qualifiers = new ArrayList<>();
}
```

### 2.3 파싱 모델 → JPA Entity 변환

**ArticleConverter.java:**
```java
package io.brillianttiger.bio.parser.converter;

import io.brillianttiger.bio.parser.pubmed.model.*;
import io.brillianttiger.bio.parser.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

    public PubmedArticleEntity toEntity(PubmedArticle article) {
        PubmedArticleEntity entity = new PubmedArticleEntity();

        // PMID
        if (article.getMedlineCitation() != null &&
            article.getMedlineCitation().getPmid() != null) {
            entity.setPmid(Long.parseLong(article.getMedlineCitation().getPmid().getValue()));
        }

        // Article Title
        if (article.getMedlineCitation().getArticle() != null) {
            Article art = article.getMedlineCitation().getArticle();
            entity.setTitle(art.getArticleTitle());

            // Abstract
            if (art.getAbstract() != null && art.getAbstract().getAbstractTexts() != null) {
                String abstractText = art.getAbstract().getAbstractTexts().stream()
                    .map(AbstractText::getValue)
                    .collect(Collectors.joining("\n"));
                entity.setAbstractText(abstractText);
            }

            // Journal Info
            if (art.getJournal() != null) {
                Journal journal = art.getJournal();
                entity.setJournalTitle(journal.getTitle() != null ?
                    journal.getTitle().getValue() : null);
                entity.setJournalIssn(journal.getIssn() != null ?
                    journal.getIssn().getValue() : null);

                // Publication Date
                if (journal.getJournalIssue() != null &&
                    journal.getJournalIssue().getPubDate() != null) {
                    PubDate pubDate = journal.getJournalIssue().getPubDate();
                    if (pubDate.getYear() != null) {
                        entity.setPublicationYear(Integer.parseInt(pubDate.getYear().getValue()));
                    }
                    if (pubDate.getMonth() != null) {
                        entity.setPublicationMonth(pubDate.getMonth().getValue());
                    }
                    if (pubDate.getDay() != null) {
                        entity.setPublicationDay(Integer.parseInt(pubDate.getDay().getValue()));
                    }
                }
            }

            // Authors
            if (art.getAuthorList() != null && art.getAuthorList().getAuthors() != null) {
                int order = 0;
                for (Author author : art.getAuthorList().getAuthors()) {
                    AuthorEntity authorEntity = new AuthorEntity();
                    authorEntity.setArticle(entity);
                    authorEntity.setLastName(author.getLastName() != null ?
                        author.getLastName().getValue() : null);
                    authorEntity.setForeName(author.getForeName() != null ?
                        author.getForeName().getValue() : null);
                    authorEntity.setInitials(author.getInitials() != null ?
                        author.getInitials().getValue() : null);
                    authorEntity.setAuthorOrder(order++);
                    entity.getAuthors().add(authorEntity);
                }
            }
        }

        // MeSH Headings
        if (article.getMedlineCitation().getMeshHeadingList() != null) {
            for (MeshHeading mesh : article.getMedlineCitation().getMeshHeadingList()) {
                MeshHeadingEntity meshEntity = new MeshHeadingEntity();
                meshEntity.setArticle(entity);

                if (mesh.getDescriptorName() != null) {
                    meshEntity.setDescriptorName(mesh.getDescriptorName().getValue());
                    meshEntity.setDescriptorUi(mesh.getDescriptorName().getUi());
                    meshEntity.setMajorTopicYN("Y".equals(mesh.getDescriptorName().getMajorTopicYN()));
                }

                entity.getMeshHeadings().add(meshEntity);
            }
        }

        return entity;
    }
}
```

---

## 3. DB 스키마 설계

### 3.1 PostgreSQL 스키마

```sql
-- PubMed Articles 테이블
CREATE TABLE pubmed_articles (
    pmid BIGINT PRIMARY KEY,
    title TEXT,
    abstract_text TEXT,
    journal_title VARCHAR(500),
    journal_issn VARCHAR(20),
    publication_year INTEGER,
    publication_month VARCHAR(20),
    publication_day INTEGER,
    raw_xml TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스
CREATE INDEX idx_title ON pubmed_articles USING gin(to_tsvector('english', title));
CREATE INDEX idx_abstract ON pubmed_articles USING gin(to_tsvector('english', abstract_text));
CREATE INDEX idx_pub_year ON pubmed_articles(publication_year);
CREATE INDEX idx_journal ON pubmed_articles(journal_title);

-- Authors 테이블
CREATE TABLE pubmed_authors (
    id BIGSERIAL PRIMARY KEY,
    pmid BIGINT NOT NULL REFERENCES pubmed_articles(pmid) ON DELETE CASCADE,
    last_name VARCHAR(200),
    fore_name VARCHAR(200),
    initials VARCHAR(20),
    affiliation TEXT,
    author_order INTEGER,
    CONSTRAINT fk_article FOREIGN KEY (pmid) REFERENCES pubmed_articles(pmid)
);

CREATE INDEX idx_author_pmid ON pubmed_authors(pmid);
CREATE INDEX idx_author_name ON pubmed_authors(last_name, fore_name);

-- MeSH Headings 테이블
CREATE TABLE pubmed_mesh_headings (
    id BIGSERIAL PRIMARY KEY,
    pmid BIGINT NOT NULL REFERENCES pubmed_articles(pmid) ON DELETE CASCADE,
    descriptor_name VARCHAR(500),
    descriptor_ui VARCHAR(20),
    major_topic_yn BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_article_mesh FOREIGN KEY (pmid) REFERENCES pubmed_articles(pmid)
);

CREATE INDEX idx_mesh_pmid ON pubmed_mesh_headings(pmid);
CREATE INDEX idx_mesh_ui ON pubmed_mesh_headings(descriptor_ui);
CREATE INDEX idx_mesh_name ON pubmed_mesh_headings(descriptor_name);

-- MeSH Qualifiers 테이블
CREATE TABLE mesh_qualifiers (
    mesh_heading_id BIGINT NOT NULL REFERENCES pubmed_mesh_headings(id) ON DELETE CASCADE,
    qualifier_name VARCHAR(500),
    qualifier_ui VARCHAR(20)
);

CREATE INDEX idx_qualifier_mesh ON mesh_qualifiers(mesh_heading_id);
```

### 3.2 PMC Articles 스키마

```sql
-- PMC Articles 테이블
CREATE TABLE pmc_articles (
    pmc_id VARCHAR(20) PRIMARY KEY,
    pmid BIGINT,
    doi VARCHAR(200),
    publisher_id VARCHAR(200),
    article_title TEXT,
    journal_title VARCHAR(500),
    journal_issn VARCHAR(20),
    publisher_name VARCHAR(200),
    publication_year INTEGER,
    abstract_text TEXT,
    body_text TEXT, -- 전체 본문
    raw_xml TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스
CREATE INDEX idx_pmc_pmid ON pmc_articles(pmid);
CREATE INDEX idx_pmc_doi ON pmc_articles(doi);
CREATE INDEX idx_pmc_title ON pmc_articles USING gin(to_tsvector('english', article_title));
CREATE INDEX idx_pmc_body ON pmc_articles USING gin(to_tsvector('english', body_text));

-- PMC Authors
CREATE TABLE pmc_authors (
    id BIGSERIAL PRIMARY KEY,
    pmc_id VARCHAR(20) NOT NULL REFERENCES pmc_articles(pmc_id) ON DELETE CASCADE,
    surname VARCHAR(200),
    given_names VARCHAR(200),
    email VARCHAR(200),
    author_order INTEGER
);

CREATE INDEX idx_pmc_author_pmc ON pmc_authors(pmc_id);
```

---

## 4. 실제 저장 코드

### 4.1 Repository 인터페이스

```java
package io.brillianttiger.bio.parser.repository;

import io.brillianttiger.bio.parser.entity.PubmedArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PubmedArticleRepository extends JpaRepository<PubmedArticleEntity, Long> {

    // PMID로 검색
    PubmedArticleEntity findByPmid(Long pmid);

    // 제목으로 검색 (Full Text Search)
    @Query(value = "SELECT * FROM pubmed_articles WHERE to_tsvector('english', title) @@ plainto_tsquery('english', ?1)",
           nativeQuery = true)
    List<PubmedArticleEntity> searchByTitle(String keyword);

    // 년도 범위로 검색
    List<PubmedArticleEntity> findByPublicationYearBetween(Integer startYear, Integer endYear);

    // 저널로 검색
    List<PubmedArticleEntity> findByJournalTitle(String journalTitle);
}
```

### 4.2 Service 클래스

```java
package io.brillianttiger.bio.parser.service;

import io.brillianttiger.bio.parser.pubmed.PubmedXmlParser;
import io.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import io.brillianttiger.bio.parser.converter.ArticleConverter;
import io.brillianttiger.bio.parser.entity.PubmedArticleEntity;
import io.brillianttiger.bio.parser.repository.PubmedArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PubmedLoaderService {

    private final PubmedXmlParser parser;
    private final ArticleConverter converter;
    private final PubmedArticleRepository repository;

    /**
     * PubMed XML 파일을 파싱하여 DB에 저장
     *
     * @param xmlPath PubMed XML 파일 경로 (gzip 지원)
     * @return 저장된 Article 수
     */
    @Transactional
    public int loadPubmedFile(Path xmlPath) {
        AtomicInteger count = new AtomicInteger(0);

        log.info("Loading PubMed file: {}", xmlPath);

        try {
            parser.parseStream(xmlPath, article -> {
                try {
                    // 1. 파싱 모델 → JPA Entity 변환
                    PubmedArticleEntity entity = converter.toEntity(article);

                    // 2. DB 저장 (기존 PMID면 업데이트)
                    repository.save(entity);

                    // 3. 카운트 증가
                    int current = count.incrementAndGet();

                    // 4. 진행 상황 로그 (1000건마다)
                    if (current % 1000 == 0) {
                        log.info("Processed {} articles", current);
                    }

                } catch (Exception e) {
                    log.error("Failed to save article: {}", article.getMedlineCitation().getPmid(), e);
                }
            });

            log.info("Successfully loaded {} articles", count.get());
            return count.get();

        } catch (Exception e) {
            log.error("Failed to load PubMed file: {}", xmlPath, e);
            throw new RuntimeException("Failed to load PubMed file", e);
        }
    }

    /**
     * 배치 저장 (성능 최적화)
     *
     * @param xmlPath PubMed XML 파일 경로
     * @param batchSize 배치 크기 (default: 100)
     */
    @Transactional
    public int loadPubmedFileBatch(Path xmlPath, int batchSize) {
        AtomicInteger count = new AtomicInteger(0);
        List<PubmedArticleEntity> batch = new ArrayList<>(batchSize);

        try {
            parser.parseStream(xmlPath, article -> {
                PubmedArticleEntity entity = converter.toEntity(article);
                batch.add(entity);

                // 배치 크기만큼 모이면 일괄 저장
                if (batch.size() >= batchSize) {
                    repository.saveAll(batch);
                    count.addAndGet(batch.size());
                    log.info("Saved batch: {} articles total", count.get());
                    batch.clear();
                }
            });

            // 남은 데이터 저장
            if (!batch.isEmpty()) {
                repository.saveAll(batch);
                count.addAndGet(batch.size());
            }

            log.info("Successfully loaded {} articles", count.get());
            return count.get();

        } catch (Exception e) {
            log.error("Failed to load PubMed file: {}", xmlPath, e);
            throw new RuntimeException("Failed to load PubMed file", e);
        }
    }
}
```

### 4.3 사용 예시

```java
package io.brillianttiger.bio.parser;

import io.brillianttiger.bio.parser.service.PubmedLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;

@SpringBootApplication
@RequiredArgsConstructor
public class PubmedLoaderApplication implements CommandLineRunner {

    private final PubmedLoaderService loaderService;

    public static void main(String[] args) {
        SpringApplication.run(PubmedLoaderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java -jar app.jar <pubmed-xml-file>");
            System.exit(1);
        }

        Path xmlFile = Path.of(args[0]);

        // 배치 저장 (100건씩)
        int count = loaderService.loadPubmedFileBatch(xmlFile, 100);

        System.out.println("✅ Successfully loaded " + count + " articles to database");
    }
}
```

---

## 5. Spring Batch 통합

### 5.1 의존성 추가

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-batch'
}
```

### 5.2 Batch Job 설정

```java
package io.brillianttiger.bio.parser.batch;

import io.brillianttiger.bio.parser.pubmed.PubmedXmlParser;
import io.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import io.brillianttiger.bio.parser.converter.ArticleConverter;
import io.brillianttiger.bio.parser.entity.PubmedArticleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import java.nio.file.Path;
import java.util.Iterator;

@Configuration
@RequiredArgsConstructor
public class PubmedBatchConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final ArticleConverter converter;

    @Bean
    public Job pubmedLoadJob(JobRepository jobRepository, Step pubmedLoadStep) {
        return new JobBuilder("pubmedLoadJob", jobRepository)
            .start(pubmedLoadStep)
            .build();
    }

    @Bean
    public Step pubmedLoadStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager) {
        return new StepBuilder("pubmedLoadStep", jobRepository)
            .<PubmedArticle, PubmedArticleEntity>chunk(100, transactionManager)
            .reader(pubmedReader())
            .processor(item -> converter.toEntity(item))
            .writer(pubmedWriter())
            .build();
    }

    @Bean
    public ItemReader<PubmedArticle> pubmedReader() {
        return new ItemReader<PubmedArticle>() {
            private Iterator<PubmedArticle> iterator;

            @Override
            public PubmedArticle read() throws Exception {
                if (iterator == null) {
                    // XML 파싱 시작
                    PubmedXmlParser parser = new PubmedXmlParser();
                    List<PubmedArticle> articles = new ArrayList<>();
                    parser.parseStream(
                        Path.of("pubmed25n0001.xml.gz"),
                        articles::add
                    );
                    iterator = articles.iterator();
                }

                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }

    @Bean
    public ItemWriter<PubmedArticleEntity> pubmedWriter() {
        JpaItemWriter<PubmedArticleEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
```

---

## 6. 검색 쿼리 예시

### 6.1 기본 검색

```java
// PMID로 검색
PubmedArticleEntity article = repository.findByPmid(1L);

// 제목으로 Full Text Search
List<PubmedArticleEntity> results = repository.searchByTitle("methanol poisoning");

// 년도 범위 검색
List<PubmedArticleEntity> articles = repository.findByPublicationYearBetween(2020, 2024);
```

### 6.2 복잡한 검색 (QueryDSL)

```java
@Repository
public class PubmedSearchRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PubmedArticleEntity> searchArticles(
        String keyword,
        Integer year,
        String journalTitle,
        List<String> meshUis
    ) {
        QP ubmedArticleEntity article = QPubmedArticleEntity.pubmedArticleEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null) {
            builder.and(article.title.containsIgnoreCase(keyword)
                .or(article.abstractText.containsIgnoreCase(keyword)));
        }

        if (year != null) {
            builder.and(article.publicationYear.eq(year));
        }

        if (journalTitle != null) {
            builder.and(article.journalTitle.eq(journalTitle));
        }

        if (meshUis != null && !meshUis.isEmpty()) {
            builder.and(article.meshHeadings.any().descriptorUi.in(meshUis));
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(article)
            .where(builder)
            .fetch();
    }
}
```

---

## 7. 성능 최적화 팁

### 7.1 배치 저장

```java
// application.yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 100
        order_inserts: true
        order_updates: true
```

### 7.2 인덱스 활용

```sql
-- Full Text Search 인덱스
CREATE INDEX idx_title_fts ON pubmed_articles USING gin(to_tsvector('english', title));
CREATE INDEX idx_abstract_fts ON pubmed_articles USING gin(to_tsvector('english', abstract_text));

-- 복합 인덱스
CREATE INDEX idx_year_journal ON pubmed_articles(publication_year, journal_title);
```

### 7.3 파티셔닝

```sql
-- 년도별 파티셔닝
CREATE TABLE pubmed_articles (
    pmid BIGINT,
    publication_year INTEGER,
    ...
) PARTITION BY RANGE (publication_year);

CREATE TABLE pubmed_articles_2020 PARTITION OF pubmed_articles
    FOR VALUES FROM (2020) TO (2021);

CREATE TABLE pubmed_articles_2021 PARTITION OF pubmed_articles
    FOR VALUES FROM (2021) TO (2022);
```

---

## 8. 실행 방법

### 8.1 단독 실행

```bash
# Gradle으로 실행
./gradlew bootRun --args="pubmed25n0001.xml.gz"

# JAR로 실행
java -jar build/libs/pubmed-loader.jar pubmed25n0001.xml.gz
```

### 8.2 Spring Batch 실행

```bash
# Batch Job 실행
java -jar build/libs/pubmed-loader.jar \
  --spring.batch.job.names=pubmedLoadJob \
  --file.path=pubmed25n0001.xml.gz
```

---

## 🎯 요약

| 방법 | 장점 | 단점 | 추천 용도 |
|------|------|------|-----------|
| **JSON 저장** | 간단, 유연 | 검색 어려움 | 원본 보관, 백업 |
| **JPA Entity** | 객체지향적, Spring 통합 | 복잡한 스키마 필요 | 일반적인 웹앱 |
| **Spring Batch** | 대용량 처리, 재시작 가능 | 설정 복잡 | 수백만 건 처리 |
| **직접 JDBC** | 최고 성능 | 코드 복잡 | 초고속 처리 필요 시 |

**추천:** **JPA + Spring Batch** 조합으로 시작!

---

**작성일:** 2026-01-22 11:35:00
**버전:** 1.0.0
