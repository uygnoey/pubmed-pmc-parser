rootProject.name = "pubmed-pmc-parser"

// 서브모듈 포함
include("common")
include("pubmed")
include("pmc")

// 서브모듈 프로젝트 디렉토리 설정
project(":common").projectDir = file("common")
project(":pubmed").projectDir = file("pubmed")
project(":pmc").projectDir = file("pmc")