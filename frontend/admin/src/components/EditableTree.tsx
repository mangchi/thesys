import React, { useState, useCallback } from 'react';
import {
  Box,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  IconButton,
  Typography,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Collapse,
  Paper,
} from '@mui/material';
import {
  ExpandLess as ExpandLessIcon,
  ExpandMore as ExpandMoreIcon,
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';

// 1) 트리 노드 타입 정의
interface TreeNode {
  id: string;
  name: string;
  children: TreeNode[];
}

// 2) 간단 ID 생성기 (운영 시에는 UUID 사용 권장)
let nextId = 1;
const genId = () => `node-${nextId++}`;

// 3) Dialog 모드 타입
type DialogMode = 'addRoot' | 'addChild' | 'edit' | null;

export default function EditableTree() {
  // 4) 상태: 트리 데이터, 다이얼로그 상태 등
  const [treeData, setTreeData] = useState<TreeNode[]>([]);
  const [expandedIds, setExpandedIds] = useState<Set<string>>(new Set());

  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<DialogMode>(null);
  const [targetNodeId, setTargetNodeId] = useState<string | null>(null);
  const [inputValue, setInputValue] = useState('');

  // 5) 재귀적으로 트리 탐색 / 업데이트 헬퍼
  const updateNodeRecursively = useCallback(
    (nodes: TreeNode[], targetId: string, fn: (node: TreeNode) => TreeNode | null): TreeNode[] => {
      return nodes
        .map((node) => {
          if (node.id === targetId) {
            const result = fn(node);
            return result;
          }
          if (node.children.length > 0) {
            return {
              ...node,
              children: updateNodeRecursively(node.children, targetId, fn),
            };
          }
          return node;
        })
        .filter(Boolean) as TreeNode[];
    },
    [],
  );

  // 6) Dialog 열기 함수들
  const openAddRootDialog = () => {
    setDialogMode('addRoot');
    setInputValue('');
    setTargetNodeId(null);
    setDialogOpen(true);
  };

  const openAddChildDialog = (parentId: string) => {
    setDialogMode('addChild');
    setTargetNodeId(parentId);
    setInputValue('');
    setDialogOpen(true);
  };

  const openEditDialog = (nodeId: string, currentName: string) => {
    setDialogMode('edit');
    setTargetNodeId(nodeId);
    setInputValue(currentName);
    setDialogOpen(true);
  };

  // 7) Dialog 확인 시 로직
  const handleDialogConfirm = () => {
    const name = inputValue.trim();
    if (!name) {
      setDialogOpen(false);
      return;
    }

    if (dialogMode === 'addRoot') {
      const newRoot: TreeNode = { id: genId(), name, children: [] };
      setTreeData((prev) => [...prev, newRoot]);
    } else if (dialogMode === 'addChild' && targetNodeId) {
      const newChild: TreeNode = { id: genId(), name, children: [] };
      setTreeData((prev) =>
        updateNodeRecursively(prev, targetNodeId, (node) => ({
          ...node,
          children: [...node.children, newChild],
        })),
      );
      // 자동으로 부모 노드를 펼치기
      setExpandedIds((prev) => new Set(prev).add(targetNodeId));
    } else if (dialogMode === 'edit' && targetNodeId) {
      setTreeData((prev) =>
        updateNodeRecursively(prev, targetNodeId, (node) => ({
          ...node,
          name,
        })),
      );
    }

    // 상태 초기화
    setDialogOpen(false);
    setDialogMode(null);
    setTargetNodeId(null);
    setInputValue('');
  };

  const handleDialogCancel = () => {
    setDialogOpen(false);
    setDialogMode(null);
    setTargetNodeId(null);
    setInputValue('');
  };

  // 8) 노드 삭제
  const handleDelete = (nodeId: string) => {
    if (!window.confirm('이 노드를 삭제하시겠습니까?')) return;
    setTreeData((prev) => updateNodeRecursively(prev, nodeId, () => null!));
    // 삭제된 노드가 확장 상태에 있으면 Set에서 제거해 두기
    setExpandedIds((prev) => {
      const copy = new Set(prev);
      copy.delete(nodeId);
      return copy;
    });
  };

  // 9) 노드 확장/축소 토글
  const handleToggle = (nodeId: string) => {
    setExpandedIds((prev) => {
      const copy = new Set(prev);
      if (copy.has(nodeId)) copy.delete(nodeId);
      else copy.add(nodeId);
      return copy;
    });
  };

  // 10) 재귀적으로 리스트 형태의 트리 렌더링
  const renderTreeList = (nodes: TreeNode[], depth = 0) =>
    nodes.map((node) => {
      const isExpanded = expandedIds.has(node.id);
      const hasChildren = node.children.length > 0;

      return (
        <Box key={node.id}>
          <ListItem
            sx={{
              pl: 2 + depth * 2, // 들여쓰기: depth마다 left padding 추가
            }}
            secondaryAction={
              <Box>
                {/* 자식 추가 */}
                <IconButton
                  edge="end"
                  size="small"
                  onClick={(e) => {
                    e.stopPropagation();
                    openAddChildDialog(node.id);
                  }}
                  title="자식 추가"
                >
                  <AddIcon fontSize="small" />
                </IconButton>
                {/* 편집 */}
                <IconButton
                  edge="end"
                  size="small"
                  onClick={(e) => {
                    e.stopPropagation();
                    openEditDialog(node.id, node.name);
                  }}
                  title="수정"
                >
                  <EditIcon fontSize="small" />
                </IconButton>
                {/* 삭제 */}
                <IconButton
                  edge="end"
                  size="small"
                  onClick={(e) => {
                    e.stopPropagation();
                    handleDelete(node.id);
                  }}
                  title="삭제"
                >
                  <DeleteIcon fontSize="small" />
                </IconButton>
              </Box>
            }
          >
            {/* 펼치기/접기 아이콘 */}
            {hasChildren ? (
              <ListItemIcon
                onClick={(e) => {
                  e.stopPropagation();
                  handleToggle(node.id);
                }}
                sx={{ minWidth: 0, mr: 1, cursor: 'pointer' }}
              >
                {isExpanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
              </ListItemIcon>
            ) : (
              <Box sx={{ width: 24, ml: 1 }} /> // 아이콘 자리 차지
            )}
            <ListItemText primary={node.name} />
          </ListItem>

          {/* 자식 노드를 Collapse로 감싸서 확장/축소 애니메이션 */}
          {hasChildren && (
            <Collapse in={isExpanded} timeout="auto" unmountOnExit>
              <List disablePadding>{renderTreeList(node.children, depth + 1)}</List>
            </Collapse>
          )}
        </Box>
      );
    });

  return (
    <Box>
      {/* 설명 및 레이아웃 */}
      <Paper sx={{ p: 2, mb: 2 }}>
        <Typography variant="h6">동적 리스트 트리 예제</Typography>
      </Paper>

      {/* 루트 노드 추가 버튼 */}
      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={openAddRootDialog}
        sx={{ mb: 2 }}
      >
        부모 노드 추가
      </Button>

      {/* 트리가 비어 있으면 안내 글, 아니면 List 렌더링 */}
      {treeData.length === 0 ? (
        <Typography variant="body2" color="text.secondary">
          노드가 없습니다. “부모 노드 추가” 버튼을 눌러 시작하세요.
        </Typography>
      ) : (
        <List disablePadding>{renderTreeList(treeData)}</List>
      )}

      {/* MUI Dialog (_노드 이름_ 입력/수정) */}
      <Dialog open={dialogOpen} onClose={handleDialogCancel} maxWidth="xs" fullWidth>
        <DialogTitle>
          {dialogMode === 'addRoot' && '루트 노드 추가'}
          {dialogMode === 'addChild' && '자식 노드 추가'}
          {dialogMode === 'edit' && '노드 이름 수정'}
        </DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="노드 이름"
            fullWidth
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                e.preventDefault();
                handleDialogConfirm();
              }
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogCancel}>취소</Button>
          <Button onClick={handleDialogConfirm} variant="contained">
            확인
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
