FROM node:23-alpine
WORKDIR /app
COPY . .
RUN npm install && npm run build && npm install -g serve
CMD ["serve", "-s", "dist", "-l", "3001"]