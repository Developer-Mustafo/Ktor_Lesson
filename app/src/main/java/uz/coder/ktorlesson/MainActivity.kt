package uz.coder.ktorlesson

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import uz.coder.ktorlesson.ui.theme.KtorLessonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KtorLessonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var list by remember {
        mutableStateOf<List<Response>>(emptyList())
    }
    LaunchedEffect(true) {
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        list = try {
            client.get("https://jsonplaceholder.typicode.com/posts").body<List<Response>>()
        }catch (e:Exception){ emptyList() }
        Log.d(TAG, "Greeting: $list")
    }
        LazyColumn(modifier = modifier) {
            itemsIndexed(list){_, item->
                Item(item = item)
            }
        }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Item(modifier: Modifier = Modifier, item:Response) {
    val context = LocalContext.current
    Card(onClick = { Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show() }, modifier = modifier.fillMaxWidth().padding(5.dp)) {
        Column(modifier = modifier.fillMaxWidth()) {
            Spacer(modifier = modifier.height(5.dp))
            Text(text = item.userId.toString(), modifier = modifier.fillMaxWidth().padding(horizontal = 5.dp), fontSize = 15.sp)
            Spacer(modifier = modifier.height(5.dp))
            Text(text = item.title.toString(), modifier = modifier.fillMaxWidth().padding(horizontal = 5.dp), fontSize = 15.sp)
            Spacer(modifier = modifier.height(5.dp))
            Text(text = item.body.toString(), modifier = modifier.fillMaxWidth().padding(horizontal = 5.dp), fontSize = 15.sp)
            Spacer(modifier = modifier.height(5.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KtorLessonTheme {
        Greeting()
    }
}
private const val TAG = "MainActivity"