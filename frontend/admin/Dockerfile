FROM node:23-alpine
WORKDIR /app

# 보안 패치 적용
RUN apk update && apk upgrade --no-cache
COPY . .
RUN npm install && npm run build && npm install -g serve
CMD ["serve", "-s", "dist", "-l", "3001"]