import React from 'react';
import { Container, Typography, Paper } from '@mui/material';
import EditableTree from '../components/EditableTree';
import DraggableTree from '../components/DraggableTree';

export default function TreeDemo() {
  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        트리 구조 예제 화면
      </Typography>

      <Paper elevation={3} sx={{ p: 2 }}>
        <EditableTree />
      </Paper>
      <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
        <DraggableTree />
      </Container>
    </Container>
  );
}
