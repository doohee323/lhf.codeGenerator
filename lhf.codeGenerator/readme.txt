1. properties/database.properties에 DB 설정

2. properties/query.properties에

PROJECT_NAME=testProject
PACKAGE_NAME=test.model
DOMAIN_NAME=TrdSmbTm05
select
		*
from
		dwe_com_auth da ,
	DWE_COM_AUTH_MENU dm
				where da.auth_code = dm.auth_code
형식으로 생성한다.
PROJECT_NAME : output폴더 아래 생성되는 폴더 명
PACKAGE_NAME : model의 패키지 경로 풀패키지 경로를 적어 주고 output\PROJECT_NAME폴더 아래 생성이 된다.
