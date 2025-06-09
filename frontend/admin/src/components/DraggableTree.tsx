import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Button,
  Collapse,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Paper,
  TextField,
  Typography,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  ExpandLess as ExpandLessIcon,
  ExpandMore as ExpandMoreIcon,
} from '@mui/icons-material';

// dnd-kit core & sortable
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
  DragEndEvent,
  DragOverlay,
  MeasuringStrategy,
} from '@dnd-kit/core';

import {
  arrayMove,
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
  rectSortingStrategy,
  defaultAnimateLayoutChanges,
} from '@dnd-kit/sortable';

import { CSS } from '@dnd-kit/utilities';

/** 1) 트리의 원본 타입 (Nested 구조) */
interface TreeNode {
  id: string;
  name: string;
  children: TreeNode[];
}

/** 2) Flat Tree 타입 */
interface FlatTreeNode {
  id: string;
  parentId: string | null;
  depth: number;
  name: string;
  index: number; // 같은 레벨 안에서의 순서
  childrenCount: number; // 하위 총 자식 개수 (필요 시)
}

/** 3) 다이얼로그 모드 */
type DialogMode = 'addRoot' | 'addChild' | 'edit' | null;

/** 4) ID 생성 함수 */
let nextId = 1;
const genId = () => `node-${nextId++}`;

/**
 * 5) Nested Tree → Flat Tree로 변환
 * - 깊이(depth)와 순서(index), parentId 정보를 붙여 1차원 배열로 만든다.
 */
function flattenTree(nodes: TreeNode[], parentId: string | null = null, depth = 0): FlatTreeNode[] {
  let flat: FlatTreeNode[] = [];
  nodes.forEach((node, index) => {
    // 자기 자신 정보를 먼저 추가
    flat.push({
      id: node.id,
      parentId,
      depth,
      name: node.name,
      index,
      childrenCount: node.children.length,
    });
    // 자식이 있으면 재귀적으로 추가
    if (node.children.length > 0) {
      flat = flat.concat(flattenTree(node.children, node.id, depth + 1));
    }
  });
  return flat;
}

/**
 * 6) Flat Tree → Nested Tree로 재구성
 * - drop 이후에 flat 배열을 재조합할 때 사용
 */
function buildTree(flat: FlatTreeNode[]): TreeNode[] {
  const rootNodes: TreeNode[] = [];

  const nodesMap: Record<string, TreeNode> = {};

  // 6-1) 모든 FlatTreeNode를 TreeNode 객체로 생성해 두고
  flat.forEach((item) => {
    nodesMap[item.id] = { id: item.id, name: item.name, children: [] };
  });

  // 6-2) parentId가 null인 것은 최상위 배열에, 그 외는 부모의 children에 넣는다
  flat.forEach((item) => {
    const node = nodesMap[item.id];
    if (item.parentId === null) {
      rootNodes.push(node);
    } else {
      const parent = nodesMap[item.parentId];
      parent.children.splice(item.index, 0, node);
    }
  });

  return rootNodes;
}

/** 7) SortableItem 컴포넌트 (드래그 가능한 리스트 아이템) */
interface SortableItemProps {
  item: FlatTreeNode;
  depth: number;
  onAddChild: (parentId: string) => void;
  onEdit: (nodeId: string, currentName: string) => void;
  onDelete: (nodeId: string) => void;
  isDragging: boolean;
}

function SortableItem({
  item,
  depth,
  onAddChild,
  onEdit,
  onDelete,
  isDragging,
}: SortableItemProps) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging: itemDragging,
  } = useSortable({
    id: item.id,
    animateLayoutChanges: (args) =>
      defaultAnimateLayoutChanges({ ...args, wasDragging: args.wasDragging }),
  });

  const style: React.CSSProperties = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: itemDragging || isDragging ? 0.5 : 1,
    paddingLeft: `${depth * 16}px`, // depth만큼 들여쓰기
    cursor: 'grab',
  };

  return (
    <ListItem ref={setNodeRef} style={style} {...attributes}>
      {/* 드래그 핸들: ☰ */}
      <ListItemIcon {...listeners} sx={{ minWidth: 24, mr: 1, cursor: 'grab' }}>
        <Typography variant="body2">☰</Typography>
      </ListItemIcon>
      <ListItemText primary={item.name} />
      <IconButton
        size="small"
        onClick={(e) => {
          e.stopPropagation();
          onAddChild(item.id);
        }}
        title="자식 추가"
      >
        <AddIcon fontSize="small" />
      </IconButton>
      <IconButton
        size="small"
        onClick={(e) => {
          e.stopPropagation();
          onEdit(item.id, item.name);
        }}
        title="수정"
      >
        <EditIcon fontSize="small" />
      </IconButton>
      <IconButton
        size="small"
        onClick={(e) => {
          e.stopPropagation();
          onDelete(item.id);
        }}
        title="삭제"
      >
        <DeleteIcon fontSize="small" />
      </IconButton>
    </ListItem>
  );
}

/** 8) DraggableTree 컴포넌트 (전체) */
export default function DraggableTree() {
  // 8-1) Nested 트리 데이터
  const [treeData, setTreeData] = useState<TreeNode[]>([]);

  // 8-2) 다이얼로그 상태
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<DialogMode>(null);
  const [targetNodeId, setTargetNodeId] = useState<string | null>(null);
  const [inputValue, setInputValue] = useState('');

  // 8-3) Flat Tree 상태
  const [flatTree, setFlatTree] = useState<FlatTreeNode[]>([]);

  // 8-4) DragOverlay용 로컬 드래그 중 항목
  const [activeId, setActiveId] = useState<string | null>(null);

  // 8-5) Sensors: 마우스/터치 센서 등록
  const sensors = useSensors(useSensor(PointerSensor, { activationConstraint: { distance: 5 } }));

  /** 9) Nested → Flat 변환 */
  useEffect(() => {
    const ft = flattenTree(treeData);
    setFlatTree(ft);
  }, [treeData]);

  /** 10) 드래그 종료 시 처리
   * - active.id: 드래그 시작된 아이템 ID
   * - over?.id: 드롭된 위치에 있는 아이템 ID
   */
  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;
    setActiveId(null);

    if (!over || active.id === over.id) {
      return;
    }

    const oldIndex = flatTree.findIndex((n) => n.id === active.id);
    const newIndex = flatTree.findIndex((n) => n.id === over.id);
    if (oldIndex < 0 || newIndex < 0) return;

    // 10-1) 같은 depth(깊이)인 경우, 레벨 내에서만 순서 변경
    if (flatTree[oldIndex].depth === flatTree[newIndex].depth) {
      const newFlat = arrayMove(flatTree, oldIndex, newIndex).map((item, idx) => ({
        ...item,
        index: idx,
      }));
      const newNested = buildTree(newFlat);
      setTreeData(newNested);
      return;
    }

    // 10-2) depth가 다른 경우 (예: 부모 ↔ 자식 이동)
    // over 노드의 parentId를 가져와서 active 노드를 같은 레벨로 옮긴 뒤,
    // 새로운 parentId (over의 parentId) 안에 active를 index 위치에 삽입.
    const overNode = flatTree[newIndex];
    const activeNode = flatTree[oldIndex];

    // 예를 들어 “overNode”를 드롭 대상으로 삼아서 activeNode가 overNode의 바로 위 혹은 아래에 들어가는 간단 구현:
    const newParentId = overNode.parentId;
    const siblings = flatTree.filter((n) => n.parentId === newParentId && n.id !== activeNode.id);

    // 삽입 위치 계산: overNode가 siblings 배열의 몇 번째인가
    const insertIndex = siblings.findIndex((n) => n.id === overNode.id);
    const reorderedSiblings = [
      // insertIndex 위치 앞부분
      ...siblings.slice(0, insertIndex),
      // activeNode
      {
        ...activeNode,
        parentId: newParentId,
        depth: overNode.depth,
      },
      // insertIndex 이후 부분
      ...siblings.slice(insertIndex),
    ];

    // 최종 새로운 flat tree 생성 (동일 parentId를 가진 노드들은 reorderedSiblings 순으로, 나머지는 그대로)
    const newFlat = flatTree
      .filter((n) => n.parentId !== newParentId && n.id !== activeNode.id)
      .concat(reorderedSiblings)
      .map((item, idx) => ({ ...item, index: idx }));

    // depth 조정: 부모가 바뀐 activeNode 밑의 자식들도 depth 조정해야 함
    // 간단화를 위해 activeNode의 자식 전체를 recalc:
    const adjustDepthRecursively = (allFlat: FlatTreeNode[], parent: FlatTreeNode) => {
      const children = allFlat.filter((n) => n.parentId === parent.id);
      children.forEach((child) => {
        child.depth = parent.depth + 1;
        adjustDepthRecursively(allFlat, child);
      });
    };
    // activeNode 재검색 후
    const movedNode = newFlat.find((n) => n.id === activeNode.id)!;
    movedNode.depth = overNode.depth;
    adjustDepthRecursively(newFlat, movedNode);

    // 최종 Nested 트리로 변환
    const newNested = buildTree(newFlat);
    setTreeData(newNested);
  };

  /** 11) 노드 추가/수정/삭제 함수들 */
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
  const handleDelete = (nodeId: string) => {
    if (!window.confirm('이 노드를 삭제하시겠습니까?')) return;
    // 삭제 시, 아이디와 그 자식 모두를 재귀적으로 제거
    const removeRecursively = (nodes: TreeNode[]): TreeNode[] => {
      return nodes
        .filter((n) => n.id !== nodeId)
        .map((n) => ({ ...n, children: removeRecursively(n.children) }));
    };
    setTreeData((prev) => removeRecursively(prev));
  };
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
      const addChildRecursively = (nodes: TreeNode[]): TreeNode[] => {
        return nodes.map((n) => {
          if (n.id === targetNodeId) {
            const newChild: TreeNode = { id: genId(), name, children: [] };
            return { ...n, children: [...n.children, newChild] };
          }
          return { ...n, children: addChildRecursively(n.children) };
        });
      };
      setTreeData((prev) => addChildRecursively(prev));
    } else if (dialogMode === 'edit' && targetNodeId) {
      const editRecursively = (nodes: TreeNode[]): TreeNode[] => {
        return nodes.map((n) => {
          if (n.id === targetNodeId) {
            return { ...n, name };
          }
          return { ...n, children: editRecursively(n.children) };
        });
      };
      setTreeData((prev) => editRecursively(prev));
    }
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

  /** 12) 렌더링: Nested Tree를 재귀적으로 보여주기 전, Flat Tree를 SortableContext로 감싸기 */
  return (
    <Box>
      <Paper sx={{ p: 2, mb: 2 }}>
        <Typography variant="h6">드래그 가능한 동적 트리</Typography>
        <Typography variant="body2" color="text.secondary">
          • 노드를 끌어서 같은 레벨에서 순서 변경
          <br />
          • 다른 부모 아래로 드래그하고 놓으면 “부모 변경”
          <br />
          • 노드 옆 아이콘으로 추가/수정/삭제 가능
          <br />• 노드가 접혀 있으면 자식이 보이지 않음
        </Typography>
      </Paper>

      {/* “루트 노드 추가” 버튼 */}
      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={openAddRootDialog}
        sx={{ mb: 2 }}
      >
        루트 노드 추가
      </Button>

      {/* DndContext 설정 */}
      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        measuring={{ droppable: { strategy: MeasuringStrategy.Always } }}
        onDragStart={(event) => setActiveId(event.active.id as string)}
        onDragEnd={handleDragEnd}
        onDragCancel={() => setActiveId(null)}
      >
        {/* SortableContext: Flat Tree의 ID 순서에 따라 정렬 전략 지정 */}
        <SortableContext
          items={flatTree.map((item) => item.id)}
          strategy={verticalListSortingStrategy}
        >
          <List disablePadding>
            {flatTree.map((item) => (
              <SortableItem
                key={item.id}
                item={item}
                depth={item.depth}
                isDragging={activeId === item.id}
                onAddChild={openAddChildDialog}
                onEdit={openEditDialog}
                onDelete={handleDelete}
              />
            ))}
          </List>
        </SortableContext>

        {/* DragOverlay: 드래그 중인 아이템을 커스텀 렌더링 */}
        <DragOverlay>
          {activeId ? (
            <Paper elevation={3} sx={{ p: 1 }}>
              <Typography variant="body2">
                {flatTree.find((n) => n.id === activeId)?.name}
              </Typography>
            </Paper>
          ) : null}
        </DragOverlay>
      </DndContext>

      {/* MUI Dialog (_노드 추가/수정_) */}
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
