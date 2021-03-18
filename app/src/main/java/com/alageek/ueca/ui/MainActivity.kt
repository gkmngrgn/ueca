package com.alageek.ueca.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.alageek.ueca.R
import com.alageek.ueca.models.Event
import com.alageek.ueca.models.EventSaver
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val event by rememberSaveable(stateSaver = EventSaver) { mutableStateOf(Event()) }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            AppContent(navController = navController, event = event)
        }
        composable("edit_time") {
            EditTimeContent(navController = navController, event = event)
        }
        composable("edit_description") {
            EditDescriptionContent(navController = navController, event = event)
        }
        composable("edit_links") {
            EditLinksContent(navController = navController)
        }
    }
}

@Composable
fun AppContent(navController: NavHostController, event: Event) = AppTheme {
    if (event.description.isEmpty()) {
        event.description = stringResource(id = R.string.default_description)
    }

    Column {
        TopBar(title = R.string.title_default)
        Column(
            modifier = Modifier
                .weight(8f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddCardView(
                title = stringResource(id = R.string.title_description),
                text = event.description,
                onClick = { navController.navigate("edit_description") })
            AddCardView(
                title = stringResource(id = R.string.title_time),
                text = "EDIT TIME",
                onClick = { navController.navigate("edit_time") })
            AddCardView(
                title = stringResource(id = R.string.title_links),
                text = "EDIT LINKS",
                onClick = { navController.navigate("edit_links") })
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
        ) {
            AddButton(text = stringResource(id = R.string.button_copy_to_clipboard))
        }
    }
}

@Composable
fun EditDescriptionContent(navController: NavHostController, event: Event) = AppTheme {
    var description by remember { mutableStateOf(event.description) }

    Scaffold(
        topBar = {
            TopBar(
                title = R.string.title_description,
                navButton = {
                    event.description = description
                    BackButton(navController = navController)
                })
        }
    ) {
        DescriptionTextField(description = description, onValueChange = { description = it })
    }
}

@Composable
fun EditTimeContent(navController: NavHostController, event: Event) = AppTheme {
    Scaffold(
        topBar = {
            TopBar(
                title = R.string.title_time,
                navButton = { BackButton(navController = navController) })
        }
    ) {
        Column(modifier=Modifier.padding(16.dp)) {
            Text(text = "Edit Time Content")
        }
    }
}

@Composable
fun EditLinksContent(navController: NavHostController) = AppTheme {
    Scaffold(
        topBar = {
            TopBar(
                title = R.string.title_links,
                navButton = { BackButton(navController = navController) })
        }
    ) {
        Text(text = "Edit Links Content")
    }
}

@Composable
fun TopBar(title: Int, navButton: @Composable (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        navigationIcon = navButton,
    )
}

@Composable
fun BackButton(navController: NavHostController) = IconButton(onClick = {
    navController.popBackStack()
}) {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = stringResource(R.string.desc_back)
    )
}

@Composable
fun AddCardView(title: String, text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(16.dp),
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.h5)
                Text(text = text, style = MaterialTheme.typography.body1)
            }
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
}

@Preview
@Composable
fun PreviewAddCardView() {
    AddCardView(title = "Title", text = "This is a card view.", onClick = {})
}

@Composable
fun AddButton(text: String) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(text = text.toUpperCase(Locale.ROOT))
    }
}

@Composable
fun DescriptionTextField(description: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = description,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            onValueChange = { onValueChange(it) },
        )
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize())
    }
}

@Preview
@Composable
fun PreviewDescriptionTextField() {
    DescriptionTextField(description = "A description test.", onValueChange = {})
}
