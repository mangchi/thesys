import React, { useState, useMemo } from 'react';
import { Box, Typography, TextField, Button, Stack } from '@mui/material';
import { ColDef } from 'ag-grid-community';
import { GridComponent } from '../components/GridComponent';

interface User {
  id: number;
  name: string;
  age: number;
  email: string;
  country: string;
}

const initialUsers: User[] = [
  { id: 1, name: '홍길동', age: 30, email: 'hong@example.com', country: 'Korea' },
  { id: 2, name: 'Jane Doe', age: 25, email: 'jane@sample.com', country: 'USA' },
  { id: 3, name: '김철수', age: 28, email: 'kim@demo.com', country: 'Korea' },
  { id: 4, name: 'John Smith', age: 35, email: 'john@site.com', country: 'Canada' },
];

const columnDefs: ColDef[] = [
  { field: 'id', headerName: 'ID', width: 90 },
  { field: 'name', headerName: '이름', flex: 1 },
  { field: 'age', headerName: '나이', filter: 'agNumberColumnFilter', width: 100 },
  { field: 'email', headerName: '이메일', flex: 1 },
  { field: 'country', headerName: '국가', flex: 1 },
];

export default function UserSearchPage() {
  const [query, setQuery] = useState('');
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return initialUsers;
    return initialUsers.filter(
      (user) =>
        user.name.toLowerCase().includes(q) ||
        user.email.toLowerCase().includes(q) ||
        user.country.toLowerCase().includes(q),
    );
  }, [query]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 시 리로드 방지
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* 제목 */}
      <Typography variant="h5" mb={3}>
        User
      </Typography>

      {/* 검색 폼 */}
      <Box component="form" onSubmit={handleSearch} sx={{ mb: 2 }}>
        <Stack direction="row" spacing={2}>
          <TextField
            label="검색어 (이름, 이메일, 국가)"
            variant="outlined"
            size="small"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            fullWidth
          />
          <Button type="submit" variant="contained" sx={{ whiteSpace: 'nowrap' }}>
            검색
          </Button>
        </Stack>
      </Box>

      <GridComponent columnDefs={columnDefs} rowData={filtered} height={500} />
    </Box>
  );
}
