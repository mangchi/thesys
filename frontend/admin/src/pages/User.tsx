import React, { useState, useMemo, useCallback } from 'react';
import { Box, TextField, Button, Stack, Fade, Paper, Collapse, Divider } from '@mui/material';
import { CellClickedEvent, ColDef } from 'ag-grid-community';
import { Grid } from '../components/Grid';
import { PageContainer } from '../components/PageContainer';
import { CommonText } from '../components/CommonText';
import Popup from '../components/Popup';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';

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
  const [openPopup, setOpenPopup] = useState(false);
  const [cellValue, setCellValue] = useState<any>(null);
  const [rowDataClicked, setRowDataClicked] = useState<any>(null);
  const [selectedRows, setSelectedRows] = useState<User[]>([]);
  const [users, setUsers] = useState<User[]>(initialUsers);
  const [isSearchVisible, setIsSearchVisible] = useState(false);

  const [nameQuery, setNameQuery] = useState('');
  const [emailQuery, setEmailQuery] = useState('');
  const [countryQuery, setCountryQuery] = useState('');
  const [ageRange, setAgeRange] = useState({ min: '', max: '' });

  const toggleSearch = () => {
    setIsSearchVisible(prev => !prev);
  };

  const onSelectionChanged = useCallback((event: any) => {
    const selectedNodes = event.api.getSelectedNodes();
    const selectedData = selectedNodes.map((node: any) => node.data);
    setSelectedRows(selectedData);
  }, []);

  const handleDelete = useCallback(() => {
    if (window.confirm('선택한 항목을 삭제하시겠습니까?')) {
      const selectedIds = selectedRows.map((row) => row.id);
      setUsers((prevUsers) => prevUsers.filter((user) => !selectedIds.includes(user.id)));
      setSelectedRows([]);
    }
  }, [selectedRows]);

  // 셀 클릭 핸들러
  const onCellClicked = useCallback((event: CellClickedEvent) => {
    setCellValue(event.value);
    setRowDataClicked(event.data);
    setOpenPopup(true);
  }, []);

  const onClose = useCallback(() => {
    setOpenPopup(false);
    setCellValue(null);
    setRowDataClicked(null);
  }, []);

  const filtered = useMemo(() => {
    return initialUsers.filter((user) => {
      const matchName = nameQuery.trim() === '' ||
        user.name.toLowerCase().includes(nameQuery.toLowerCase());
      const matchEmail = emailQuery.trim() === '' ||
        user.email.toLowerCase().includes(emailQuery.toLowerCase());
      const matchCountry = countryQuery.trim() === '' ||
        user.country.toLowerCase().includes(countryQuery.toLowerCase());
      const matchAge = (!ageRange.min || user.age >= Number(ageRange.min)) &&
        (!ageRange.max || user.age <= Number(ageRange.max));

      return matchName && matchEmail && matchCountry && matchAge;
    });
  }, [nameQuery, emailQuery, countryQuery, ageRange]);

  // const filtered = useMemo(() => {
  //   const q = query.trim().toLowerCase();
  //   if (!q) return initialUsers;
  //   return initialUsers.filter(
  //     (user) =>
  //       user.name.toLowerCase().includes(q) ||
  //       user.email.toLowerCase().includes(q) ||
  //       user.country.toLowerCase().includes(q),
  //   );
  // }, [query]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 시 리로드 방지
  };

  return (
    <PageContainer>

      <Box sx={{ display: 'flex' }}>
        <CommonText variantType="title"> User</CommonText>

      </Box>
      <Paper
        elevation={3}
        sx={{
          // maxWidth: 480,
          mx: 'auto',
          mt: 4,
          p: 3,
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
        }}
      >
        {/* 검색 Form OPEN */}
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-start' }}>
          <Button
            size="large"
            variant="contained"
            onClick={toggleSearch}
            sx={{ whiteSpace: 'nowrap' }}
          >
            {isSearchVisible ? <><KeyboardArrowDownIcon />search </> : <><KeyboardArrowRightIcon /> search </>}</Button>
        </Box>
        {/* 검색 폼 */}
        <Collapse in={isSearchVisible} timeout={700}>
          <Box
            component="form"
            onSubmit={handleSearch}
            sx={{ border: '1px solid #ccc', width: '100%' }}
          >
            <Box
              component="form"
              onSubmit={handleSearch}
              sx={{ p: 1, width: '100%' }}
            >
              <Stack direction="row" spacing={{ xs: 1, sm: 2 }} sx={{ p: 1 }} divider={<Divider orientation="vertical" flexItem />}>


                <TextField
                  label="이름"
                  variant="outlined"
                  size="small"
                  value={nameQuery}
                  onChange={(e) => setNameQuery(e.target.value)}
                  fullWidth
                />

                <TextField
                  label="국가"
                  variant="outlined"
                  size="small"
                  value={countryQuery}
                  onChange={(e) => setCountryQuery(e.target.value)}
                  fullWidth
                />
              </Stack>

              <Stack sx={{ p: 1 }}>
                <TextField
                  label="이메일"
                  variant="outlined"
                  size="small"
                  value={emailQuery}
                  onChange={(e) => setEmailQuery(e.target.value)}
                  fullWidth
                />
              </Stack>
              <Stack direction="row" spacing={{ xs: 1, sm: 2 }} divider={<Divider orientation="vertical" flexItem />} sx={{ p: 1 }}>

                <TextField
                  label="최소 나이"
                  type="number"
                  variant="outlined"
                  size="small"
                  value={ageRange.min}
                  onChange={(e) => setAgeRange(prev => ({ ...prev, min: e.target.value }))}
                  fullWidth
                />
                <TextField
                  label="최대 나이"
                  type="number"
                  variant="outlined"
                  size="small"
                  value={ageRange.max}
                  onChange={(e) => setAgeRange(prev => ({ ...prev, max: e.target.value }))}
                  fullWidth
                />

              </Stack>
              {/* 검색 버튼 추가 */}
              <Box sx={{ display: 'flex', justifyContent: 'flex-end', p: 1, mt: 1 }}>
                <Button
                  type="submit"
                  variant="contained"
                  size="large"

                >
                  검색
                </Button>
              </Box>
            </Box>
          </Box>
        </Collapse>
        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center', justifyContent: 'flex-end' }}>
          <Button size="large" variant="contained" sx={{ whiteSpace: 'nowrap' }}>
            +Add
          </Button>
          {selectedRows.length > 0 && (
            <Fade in={selectedRows.length > 0}>
              <Button
                size="large"
                variant="contained"
                color="error"
                sx={{ whiteSpace: 'nowrap' }}
                // disabled={selectedRows.length === 0}
                onClick={handleDelete}
              >
                {' '}
                Delete
              </Button>
            </Fade>
          )}
        </Box>
        <Grid
          className="w-[100%]"
          columnDefs={columnDefs}
          rowData={filtered}
          onCellClicked={onCellClicked}
          rowSelectionMode="multiRow"
          onSelectionChanged={onSelectionChanged}
        />
      </Paper>

      <Popup
        open={openPopup}
        onClose={onClose}
        title="셀 클릭 정보"
        content={
          <div>
            <p>
              <strong>이름:</strong> {cellValue}
            </p>
            <p>
              <strong>정보:</strong>
            </p>
            <pre>{JSON.stringify(rowDataClicked, null, 2)}</pre>
          </div>
        }
        confirmText="확인"
      ></Popup>
    </PageContainer >
  );
}
