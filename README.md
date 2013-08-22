# lhf.codeGenerator
  lhf sub project

## Objective

Spring MVC + Oracle IBATIS + Xplatform(RIA) 환경에서 개발 시 소스를 자동으로 생성해주는 프로그램

https://sites.google.com/site/pbfsup/gongjisahang/gong-yusoseujadongsaengseongdogu

## Usage

아래의 환경설정 정보에 쿼리를 입력하면 쿼리를 분석하여 필요한 소스를 생성해 줍니다.

아래와 같은 소스가 자동으로 생성됩니다.

CoUser.java
CoUserController.java
CoUserService.java
CoUserSqlMap.xml
CoUserUI.xml

아래의 설정으로 실행할 수 있습니다.

1. properties/database.properties에 DB 설정

2. properties/query.properties에

PROJECT_NAME=testProject
PACKAGE_NAME=test.model
CLASS_NAME=TrdSmbTm05
select
 *
from
 dwe_com_auth da ,
 DWE_COM_AUTH_MENU dm
 where da.auth_code = dm.auth_code
형식으로 생성한다.
PROJECTNAME : output폴더 아래 생성되는 폴더 명
PACKAGENAME : model의 패키지 경로 풀패키지 경로를 적어 주고 output\PROJECTNAME폴더 아래 생성이 된다.


## Developing

- Oracle Object 분석
- Freemarker

