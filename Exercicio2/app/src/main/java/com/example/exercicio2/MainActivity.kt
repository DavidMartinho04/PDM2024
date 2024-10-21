package com.example.exercicio2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercicio2.ui.theme.Exercicio2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercicio2Theme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ExemploListStrings(itemsList: List<String>) {
    LazyColumn {
        items (itemsList) { item ->
            Text(text = item)
        }
    }
}
    class FormLine(
    val name: String,
    val type: String,
    val hint: String
)

@Composable
fun DrawFormLine(itemsList: List<FormLine>) {
    var formValues = remember { mutableStateMapOf<String, String>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(17.dp)
    ) {
        items(itemsList) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f)
                )

                TextField(
                    value = formValues.getOrDefault(item.name, ""),
                    onValueChange = { newValue ->
                        formValues[item.name] = newValue
                    },
                    placeholder = { item.hint.let { Text(it) } },
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    val formItems = listOf(
        FormLine(name = "Username", type = "text", hint = "Enter your username"),
        FormLine(name = "Email", type = "text", hint = "Enter your email"),
        FormLine(name = "Age", type = "number", hint = "Enter your age")
    )

    MaterialTheme {
        DrawFormLine(itemsList = formItems)
    }
}