# 1단계: Build 단계
FROM node:23-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

# 2단계: 실행 환경
FROM node:23-alpine3.20 AS runner

WORKDIR /app

# Next.js needs this for image optimization
ENV NODE_ENV=production
ENV PORT=3000

# 필수 파일 복사
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
COPY --from=builder /app/next.config.ts ./next.config.ts

EXPOSE 3000

CMD ["npm", "start"]