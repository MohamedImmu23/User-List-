package com.example.simplebasicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.simplebasicapp.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UserDetailScreen(user: User, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("User Details", color = Color.White) },
                modifier = Modifier.fillMaxWidth(), // ✅ Correct Modifier
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7B61FF)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(25.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 24.dp, bottom = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))

                DetailRow(icon = Icons.Default.Person, text = "${user.firstName} ${user.lastName}")
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(icon = Icons.Default.Email, text = user.email)
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(icon = Icons.Default.Phone, text = user.phone)
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(icon = Icons.Default.LocationOn, text = "${user.city}, ${user.country}")
            }
        }
    }
}

// Reusable Detail Row Component
@Composable
fun DetailRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF7B61FF))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 18.sp)
    }
}