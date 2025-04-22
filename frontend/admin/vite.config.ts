import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: parseInt(process.env.VITE_PORT || '3001'),        // 원하는 포트로 설정
    open: true,        // 브라우저 자동 오픈 (선택)
    host: true         // 외부 IP 접근 허용 (선택)
  }
})
