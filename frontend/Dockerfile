FROM node:20-alpine
WORKDIR /app
COPY . .
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN npm install
RUN npm run build
RUN npm install -g serve
CMD ["serve", "-s", "dist", "-l", "5173"]
