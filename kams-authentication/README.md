# kams-authentication

KaMS Authentication Service.

## API Endpoints

- **Register**
  - URL : http://localhost:8085/auth/register
  - Method : POST
  - Body Parameter (Json) :
    - username
    - password
  - response (success) : <200, User created (Json)>
  - response (fail) : <400, ${error.user-already-exists}>
  - call example : 
    - curl -X POST http://localhost:8085/register -H "Content-Type: application/json" -d
      "{\"username\": \"XX\",\"password\": \"XX\"}"


- **Get Token**
  - URL : http://localhost:8085/auth/token
  - Method : POST
  - Body Parameter (Json) :
      - username
      - password
  - response (success) : <200, the generated token>
  - response (fail) : <401, ${error.invalid-access}>
  - ????????? checker les erreurs, genre bad cred
  - call example :
      - curl -X POST http://localhost:8085/token -H "Content-Type: application/json" -d
        "{\"username\": \"XX\",\"password\": \"XX\"}" 


- **Validate**
  - URL : http://localhost:8085/auth/validate
  - Method : GET
  - URL Parameter : token
  - response (success) : <200, ${token.valid}>
  - response (fail) : <401, ${error.expired-token}>
  - ????????????????????????????????????????
  - call example :
    - curl -X GET http://localhost:8085/auth/validate?token=eyJhbGciOiJIUzI1NiJ9.e......
