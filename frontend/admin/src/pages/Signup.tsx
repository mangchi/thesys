import { Box, Paper, Typography, TextField, Button, Stack } from '@mui/material';
import { Link } from 'react-router-dom';
import { CommonText } from '../components/CommonText';

export default function Signup() {
  return (
    <Box
      sx={{
        height: '100vh',
        width: '100vw',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#f4f6f8',
      }}
    >
      <Paper elevation={3} sx={{ padding: 4, width: 360 }}>
        <CommonText variantType="subtitle"> 회원가입 </CommonText>
        {/* <Typography variant="h5" gutterBottom>
          📝 회원가입
        </Typography> */}

        <Stack spacing={2}>
          <TextField label="이름" variant="outlined" fullWidth />
          <TextField label="이메일" variant="outlined" fullWidth />
          <TextField label="비밀번호" type="password" variant="outlined" fullWidth />
          <TextField label="비밀번호 확인" type="password" variant="outlined" fullWidth />
          <Button variant="contained" fullWidth>
            회원가입
          </Button>
        </Stack>

        {/* 로그인 이동 링크 */}
        <Typography variant="body2" align="center" mt={2}>
          이미 계정이 있으신가요?{' '}
          <Link to="/login" className="text-blue-650 no-underline">
            로그인
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
}
