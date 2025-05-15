import { useState, ChangeEvent, FormEvent } from 'react';
import {
    Box,
    Typography,
    TextField,
    Button,
    Avatar,
    Alert,
    Paper,
} from '@mui/material';

interface ProfileData {
    avatarUrl?: string;
    name: string;
    email: string;
    phone: string;
    address: string;
}

export default function Profile() {
    // 1) 더미 초기값
    const [form, setForm] = useState<ProfileData>({
        avatarUrl: 'https://via.placeholder.com/80',
        name: '홍길동',
        email: 'hong@example.com',
        phone: '010-1234-5678',
        address: '서울시 강남구 역삼동',
    });

    // 2) 에러/성공 메시지 로컬 상태
    const [message, setMessage] = useState<{ type: 'error' | 'success'; text: string } | null>(null);

    // 3) 입력 핸들러
    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    // 4) 제출 핸들러
    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        // 서버 호출 대신 console.log
        console.log('저장할 프로필 데이터:', form);
        setMessage({ type: 'success', text: '프로필이 로컬에 저장되었습니다.' });
        // 만약 유효성 검사 필요하면 아래처럼 설정
        // if (!form.name) { setMessage({ type: 'error', text: '이름을 입력해주세요.' }); return; }
    };

    return (
        <Paper
            elevation={3}
            sx={{
                maxWidth: 480,
                mx: 'auto',
                mt: 4,
                p: 3,
                display: 'flex',
                flexDirection: 'column',
                gap: 2,
            }}
        >
            <Typography variant="h5" textAlign="center">
                개인정보 수정
            </Typography>

            {/* 프로필 사진 */}
            <Box sx={{ display: 'flex', justifyContent: 'center', mb: 1 }}>
                <Avatar
                    src={form.avatarUrl}
                    sx={{ width: 80, height: 80 }}
                />
            </Box>

            {/* 메시지 */}
            {message && (
                <Alert severity={message.type} onClose={() => setMessage(null)}>
                    {message.text}
                </Alert>
            )}

            <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="이름"
                    name="name"
                    value={form.name}
                    onChange={handleChange}
                    fullWidth
                    required
                />
                <TextField
                    label="이메일"
                    name="email"
                    type="email"
                    value={form.email}
                    onChange={handleChange}
                    fullWidth
                    required
                />
                <TextField
                    label="전화번호"
                    name="phone"
                    value={form.phone}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="주소"
                    name="address"
                    value={form.address}
                    onChange={handleChange}
                    fullWidth
                />

                <Button type="submit" variant="contained" color="primary">
                    저장하기
                </Button>
            </Box>
        </Paper>
    );
}