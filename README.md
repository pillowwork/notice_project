# 공지사항 관리 REST API 구현

**[기능 요구사항]**

    ● 공지사항 등록, 수정, 삭제, 조회 API를 구현한다.
    
    ● 공지사항 등록시 입력 항목은 다음과 같다.
    - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
    
    ● 공지사항 조회시 응답은 다음과 같다.
    - 제목, 내용, 등록일시, 조회수, 작성자

**[비기능 요구사항 및 평가 항목]**

    ● REST API로 구현.
    
    ● 개발 언어는 Java, Kotlin 중 익숙한 개발 언어로 한다.
    
    ● 웹 프레임 워크는 Spring Boot 을 사용한다.
    
    ● Persistence 프레임 워크는 Hibernate 사용시 가산점
    
    ● 데이터 베이스는 제약 없음
    
    ● 기능 및 제약사항에 대한 단위/통합테스트 작성
    
    ● 대용량 트래픽을 고려하여 구현할 것
    
    ● 핵심 문제해결 전략 및 실행 방법등을 README 파일에 명시

---

**[ 개발 환경 ]**
>* 개발 언어 : Java 17
>* 프레임워크 : Springboot 3.4.2
>* 데이터 베이스 : H2


**[ 애플리케이션 구동 ]**
> ./gradlew bootRun

**[ 접속 URL ]**
>* Bacnend-API : http://localhost:8080
>* H2 (DB) : http://localhost:8080/h2-console

**[ Backend-API 기능 명세 ]**
* **공지사항 등록**
    * [POST] /api/notice
    * Request Header
      * Content-Type : multipart/form-data
    * Payload

      | 항목      | 필드명 | 필수 여부 | 데이터 타입                       |
      |---------|-----|-------|------------------------------|
      | 제목      | title | 필수    | string                       |
      | 내용      | contents | 필수    | string                       |
      | 공지 시작일시 | startDate | 필수    | string (yyyy-MM-dd hh:MI:ss) |
      | 공지 종료일시 | endDate | 필수    | string (yyyy-MM-dd hh:MI:ss) |
      | 작성자     | author | 필수    | string |
      | 첨부파일    | attachments | 선택    | file list |
      * Response Sample
        ```json
          {
            "title" : "TITLE_DATA",
            "contents" : "CONTENTS_DATA",
            "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
            "viewCount" : 0,
            "author" : "AUTHOR_DATA",
            "attachments" :
                [
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_1",
                        "storedFileName" : "ORIGINAL_FILE_NAME_1",
                        "filePath" : "FILE_PATH_1",
                        "fileSize" : 11111,
                        "fileType" : "FILE_TYPE_1",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    },
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_2",
                        "storedFileName" : "ORIGINAL_FILE_NAME_2",
                        "filePath" : "FILE_PATH_2",
                        "fileSize" : 22222,
                        "fileType" : "FILE_TYPE_2",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    }
                ] 
        }
        ```


* **공지사항 조회**
  * [GET] /api/notice/{id}
  * Response Sample
      ```json
          {
            "title" : "TITLE_DATA",
            "contents" : "CONTENTS_DATA",
            "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
            "viewCount" : 0,
            "author" : "AUTHOR_DATA",
            "attachments" :
                [
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_1",
                        "storedFileName" : "ORIGINAL_FILE_NAME_1",
                        "filePath" : "FILE_PATH_1",
                        "fileSize" : 11111,
                        "fileType" : "FILE_TYPE_1",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    },
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_2",
                        "storedFileName" : "ORIGINAL_FILE_NAME_2",
                        "filePath" : "FILE_PATH_2",
                        "fileSize" : 22222,
                        "fileType" : "FILE_TYPE_2",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    }
                ] 
        }
      ```


* **공지사항 수정**
  * [PUT] /api/notice/{id}
  * Request Header
    * Content-Type : multipart/form-data
  * Payload

      | 항목      | 필드명 | 필수 여부 | 데이터 타입                       |
      |---------|-----|-------|------------------------------|
      | 제목      | title | 필수    | string                       |
      | 내용      | contents | 필수    | string                       |
      | 공지 시작일시 | startDate | 필수    | string (yyyy-MM-dd hh:MI:ss) |
      | 공지 종료일시 | endDate | 필수    | string (yyyy-MM-dd hh:MI:ss) |
      | 작성자     | author | 필수    | string |
      | 첨부파일    | attachments | 선택    | file list |

  * Response Sample
      ```json
          {
            "title" : "TITLE_DATA",
            "contents" : "CONTENTS_DATA",
            "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
            "viewCount" : 0,
            "author" : "AUTHOR_DATA",
            "attachments" :
                [
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_1",
                        "storedFileName" : "ORIGINAL_FILE_NAME_1",
                        "filePath" : "FILE_PATH_1",
                        "fileSize" : 11111,
                        "fileType" : "FILE_TYPE_1",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    },
                    {
                        "originalFileName" : "ORIGINAL_FILE_NAME_2",
                        "storedFileName" : "ORIGINAL_FILE_NAME_2",
                        "filePath" : "FILE_PATH_2",
                        "fileSize" : 22222,
                        "fileType" : "FILE_TYPE_2",
                        "createDate" : "YYYY-MM-DD'T'HH:MI:SS.sss",
                    }
                ] 
        }
      ```

* **공지사항 삭제**
  * [DELETE] /api/notice/{id}

