import { Container, Paper, Typography } from "@mui/material";
import FileUploader from "../components/FileUploder";

const UploadDemo = () => {
    return (
        <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom>
                업로더 예제 화면
            </Typography>

            <Paper elevation={3} sx={{ p: 2 }}>
                <FileUploader />
            </Paper>

        </Container>
    );
}
export default UploadDemo;