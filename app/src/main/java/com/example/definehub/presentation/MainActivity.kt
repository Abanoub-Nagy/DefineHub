package com.example.definehub.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.definehub.domain.model.Meaning
import com.example.definehub.domain.model.WordItem
import com.example.definehub.ui.theme.DefineHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DefineHubTheme {
                BarColor()
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.state.collectAsState()

                MainScreen(
                    mainState = mainState,
                    onSearchWordChanged = { mainViewModel.onEvent(UiEvents.OnSearchWordChanged(it)) },
                    onSearchClicked = { mainViewModel.onEvent(UiEvents.OnSearchClicked) })
            }
        }
    }
}

@Composable
fun MainScreen(
    mainState: State,
    onSearchWordChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
                searchText = mainState.searchText,
                onSearchWordChanged = onSearchWordChanged,
                onSearchClicked = onSearchClicked
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Content(mainState = mainState)
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchWordChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val paddingHorizontal = if (screenWidth < 600.dp) 16.dp else 32.dp
    val paddingTop = if (screenWidth < 600.dp) 50.dp else 60.dp

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = paddingHorizontal,
                end = paddingHorizontal,
                top = paddingTop,
                bottom = 2.dp
            ),
        value = searchText,
        onValueChange = onSearchWordChanged,
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search a word",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onSearchClicked() }
            )
        },
        label = {
            Text(
                text = "Search a word",
                fontSize = if (screenWidth < 600.dp) 15.sp else 18.sp,
                modifier = Modifier.alpha(0.7f)
            )
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = if (screenWidth < 600.dp) 19.5.sp else 22.sp
        )
    )
}

@Composable
fun Content(mainState: State) {
    Box(modifier = Modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (screenWidth < 600.dp) 80.dp else 100.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = if (screenWidth < 600.dp) 30.dp else 50.dp)
        ) {
            mainState.word?.let { wordItem ->
                Spacer(modifier = Modifier.height(if (screenWidth < 600.dp) 20.dp else 30.dp))
                WordHeader(wordItem)
                Spacer(modifier = Modifier.height(if (screenWidth < 600.dp) 20.dp else 30.dp))
            }
        }

        Box(
            modifier = Modifier
                .padding(top = if (screenWidth < 600.dp) 110.dp else 130.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(0.7f))
        ) {
            if (mainState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(if (screenWidth < 600.dp) 80.dp else 100.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                mainState.word?.let { wordItem ->
                    WordResult(wordItem)
                }
            }
        }
    }
}

@Composable
fun WordHeader(wordItem: WordItem) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column {
        Text(
            text = wordItem.word,
            fontSize = if (screenWidth < 600.dp) 30.sp else 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = wordItem.phonetic,
            fontSize = if (screenWidth < 600.dp) 17.sp else 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun WordResult(wordItem: WordItem) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LazyColumn(
        contentPadding = PaddingValues(vertical = if (screenWidth < 600.dp) 32.dp else 40.dp)
    ) {
        items(wordItem.meanings.size) { index ->
            MeaningItem(meaning = wordItem.meanings[index], index = index)
            Spacer(modifier = Modifier.height(if (screenWidth < 600.dp) 32.dp else 40.dp))
        }
    }
}

@Composable
fun MeaningItem(meaning: Meaning, index: Int) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (screenWidth < 600.dp) 16.dp else 24.dp)
    ) {
        Text(
            text = "${index + 1}. ${meaning.partOfSpeech}",
            fontSize = if (screenWidth < 600.dp) 17.sp else 20.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(0.4f),
                            Color.Transparent
                        )
                    )
                )
                .padding(top = 2.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
        )

        if (meaning.definition.definition.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            DefinitionRow(
                label = "definition",
                text = meaning.definition.definition,
                screenWidth = screenWidth
            )
        }

        if (meaning.definition.example.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            DefinitionRow(
                label = "example",
                text = meaning.definition.example,
                screenWidth = screenWidth
            )
        }
    }
}

@Composable
fun DefinitionRow(label: String, text: String, screenWidth: Dp) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = if (screenWidth < 600.dp) 19.sp else 22.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = if (screenWidth < 600.dp) 17.sp else 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BarColor() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.background
    LaunchedEffect(color) {
        systemUiController.setSystemBarsColor(color)
    }
}
