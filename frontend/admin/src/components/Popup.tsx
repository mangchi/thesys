import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from '@mui/material';

interface PopupProps {
  open: boolean;
  onClose: () => void;
  title?: string;
  content?: React.ReactNode;
  confirmText?: string;
  cancelText?: string;
  onConfirm?: () => void;
}

const Popup = ({
  open,
  onClose,
  title,
  content,
  confirmText = '확인',
  cancelText = '취소',
  onConfirm,
}: PopupProps) => {
  const handleConfirm = () => {
    if (onConfirm) onConfirm();
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} aria-labelledby="popup-dialog-title">
      {title && <DialogTitle id="popup-dialog-title">{title}</DialogTitle>}
      <DialogContent>
        {typeof content === 'string' ? <DialogContentText>{content}</DialogContentText> : content}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          {cancelText}
        </Button>
        <Button onClick={handleConfirm} color="primary" autoFocus>
          {confirmText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default Popup;
