# Angular Front End + Java Backend + Micro Frontend Host Example

## Setup
1. Execute command `npm install` in remote app folder [app-frontend-remote](app-frontend-remote/)
2. Execute command `npm install` in host app folder [app-frontend-host](app-frontend-host/)
3. In root directory, trigger the `serve-remote.sh` to bring up the remote app
4. In root directory, trigger the `serve-host.sh` to bring up the host app
    - Swagger UI: http://localhost:8080/swagger-ui/index.html
    - Host Angular App: http://localhost:8080
    - Test remote app is loading in host: http://localhost:8080/app/remote-page
    - Manually load remote app from button click: http://localhost:8080/app/remote-load