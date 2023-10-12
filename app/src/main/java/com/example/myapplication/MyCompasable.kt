package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@Composable
fun MyComposable() {
    var pageData by remember { mutableStateOf(emptyList<Pair<String, String>>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val url = "https://jut.su"
                val document = Jsoup.connect(url).get()
                val mediaContentElements = document.select(".media_content")

                val newData = mediaContentElements.map { element ->
                    val title = element.select(".b-g-title").text()
                    val imageUrl = element.select(".media_link.l_e_m_l img").attr("src")
                    Pair(title, imageUrl)
                }

                pageData = newData
            } catch (e: Exception) {
                // Обработайте ошибку
                e.printStackTrace()
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Заголовки с веб-сайта:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(pageData) { (pageTitle, imageUrl) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = pageTitle,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
