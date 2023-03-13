ARG DOCKER_REPO
FROM ${DOCKER_REPO:-docker.io}/node:14-alpine AS builder

RUN mkdir -p /workspace

WORKDIR /workspace

COPY . /workspace

RUN npm ci && npm run build:prod

FROM ${DOCKER_REPO:-docker.io}/nginx:latest

COPY --from=builder /workspace/dist/http-gui/ /usr/share/nginx/html/
