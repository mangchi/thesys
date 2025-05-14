import React from 'react';
import {
    Card, CardContent, Typography, CircularProgress, Box, Table, TableBody,
    TableCell, TableHead, TableRow
} from '@mui/material';
import { useWeather } from '../hooks/useWeather';

export default function WeatherWidget() {
    const { data, isLoading, error } = useWeather();

    if (isLoading) {
        return (
            <Card>
                <CardContent sx={{ textAlign: 'center' }}>
                    <CircularProgress />
                    <Typography>날씨 로딩 중...</Typography>
                </CardContent>
            </Card>
        );
    }
    if (error || !data) {
        return (
            <Card>
                <CardContent>
                    <Typography color="error">단기예보 정보를 불러올 수 없습니다.</Typography>
                    {error instanceof Error && <Typography variant="caption">{error.message}</Typography>}
                </CardContent>
            </Card>
        );
    }

    return (
        <Card>
            <CardContent>
                <Typography variant="h6" gutterBottom>단기예보 (서울 기준)</Typography>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>시간</TableCell>
                            <TableCell>기온</TableCell>
                            <TableCell>구름상태</TableCell>
                            <TableCell>강수확률</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((t, idx) => (
                            <TableRow key={idx}>
                                <TableCell>{t.time}</TableCell>
                                <TableCell>{t.temperature}</TableCell>
                                <TableCell>
                                    {t.sky === '1' ? '맑음' : t.sky === '3' ? '구름많음' : '흐림'}
                                </TableCell>
                                <TableCell>{t.rainProb}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    );
}