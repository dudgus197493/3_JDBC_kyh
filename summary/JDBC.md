# JDBC
> ### JAVA에서 DB에 연결(접근)할 수 있게 해주는 **JAVA Programming API**
> *java.sql 패키지에서 제공*

## JDBC를 이용한 애플리케이션을 만들 때 필요한 것
1. ### Java의 JDBC 관련 인터페이스
   - DriverManager
   - Connection
   - Statement(PreparedStatement)
   - ResultSet
   - ...

2. ### DBMS(Oracle)
3. ### ojdbc 라이브러리
    - 오라클에서 제공하는 Java와 연결하기 위한 라이브러리
    - JDBC를 상속 받아 구현한 클래스 모음
    - OracleDriver.class 이용

## JDBC 주요 인터페이스

- ### DriverManager
  - 데이터 원본에 JDBC드라이버를 통하여 커넥션을 만드는 역할
  - `Class.forName()` 메서드를 통해 생성, **반드시 예외처리** 해야 함
  - 직접 객체 생성이 불가, `getConnection()` 메서드 사용하여 객체 생성 가능
    ```Java
    // Class.forName(드라이버 이름)
    Class.forName(oracle.jdbc.driver.OracleDriver);
    ```

- ### Connection
  - DB 연결 정보를 담은 객체
  - **Java와 DB 사이를 연결해주는 통로**
  - DBMS타입, 이름, IP, Port, 계정명, 비밀번호가 담김
    ```Java
    Connection conn = DriverManager.getConnection();
    ```

- ### Statement
  - Connection을 통해 SQL문을 DB에 전달하여 실행
  - SQL 실행 후 생성된 결과 (ResultSet, 성공한 행의 개수)를 반환하는데 사용 되는 객체
  ```Java
  Statement stmt = conn.createStatement();
  ResultSet rs = stmt.executeQuery(sql) // DB에 전달할 sql
  ```

- ### PreparedStatement
  - Statement의 자식 클래스
  - Statement와 마찬가지로 SQL실행 후 생성된 결과 반환하는데 사용되는 객체
  - 객체 생성시 메서드에 ?(placeholder)가 존재하는 sql을 매개변수로 받음
  - set자료형() 메서드로 ?안에 들어갈 값을 대입

- ### ResultSet
  - 조회 결과 집합을 나타내는 객체
  - SELECT 질의 성공 시 반환되는 객체

- ### Statement와 PreparedStatement 차이점
|구분|Statement|PreparedStatement|
|:--:|:--:|:--:|
|SQL 확인 시기|SQL수행 메서드 실행시 매개변수로 SQL을 받음|객체 생성 시 매개변수로 ?(placeholder)가 있는 sql을 받음
|임의의 값 대입|SQL문자열에 직접 대입</br>값의 타입이 문자열이라면 DB문자열 표기법 `' '`을 작성해야함|`set자료형()` 메서드를 사용하여 SQL의 ?(placeholder)자리에 들어갈 값을 대입</br>`' '`작성하지 않아도 됨

- ### Statement(PreparedStatement) 의 SQL수행 메서드
  - #### executeQuery()
    - DB에 값을 조회하기 위한 메서드
    - 반환형으로 조회결과의 집합 **ResultSet** 객체를 반환
    - SELECT
  - #### executeUpdate()
    - DB에 값을 추가, 수정, 삭제하기 위한 메서드
    - 반환형으로 성공한 결과 행 개수 반환
    - INSERT, UPDATE, DELETE