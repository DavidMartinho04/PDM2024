import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student.StudentViewModel
import com.example.student.ui.StudentItem

@Composable
fun StudentListScreen(
    studentViewModel: StudentViewModel = viewModel(),
    onAddStudentClick: () -> Unit // Parâmetro para navegação
) {
    val students by studentViewModel.students.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título da página
        Text(
            text = "Estudantes IPCA",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 25.dp)
                .align(Alignment.CenterHorizontally)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(students) { student ->
                StudentItem(student)
            }
        }

        // Botão para adicionar estudante
        SmallButton(
            onClick = onAddStudentClick, // Navega para a página de adição de estudante
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun SmallButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(45.dp)
            .widthIn(min = 45.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "+",
            fontSize = 21.sp
        )
    }
}
