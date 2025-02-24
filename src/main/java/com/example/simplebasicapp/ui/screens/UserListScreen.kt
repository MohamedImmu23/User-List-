package com.example.simplebasicapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.simplebasicapp.data.model.User
import com.example.simplebasicapp.ui.animation.FadeInAnimation
import com.example.simplebasicapp.viewmodel.UserViewModel
import com.example.simplebasicapp.viewmodel.WeatherViewModel
import com.google.gson.Gson

@Composable
fun UserListScreen(
    viewModel: UserViewModel, weatherViewModel: WeatherViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val users = viewModel.pagedUsers.collectAsLazyPagingItems()

    LaunchedEffect(searchQuery) {
        viewModel.setSearchQuery(searchQuery)
    }


    LaunchedEffect(users) {
        snapshotFlow { users.itemCount }.collect { itemCount ->
            if (itemCount > 0) {
                android.util.Log.d("UserListScreen", "Users loaded: $itemCount")
            }
        }
    }

    FadeInAnimation {
        Column (modifier = Modifier.padding(paddingValues)){
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F1F1),
                    unfocusedContainerColor = Color(0xFFF1F1F1),
                    disabledContainerColor = Color(0xFFF1F1F1),
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(16.dp))
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users.itemCount) { index ->
                    val user = users[index]
                    user?.let {
                        UserItem(it, navController)
                    }
                }

                item {
                    when (users.loadState.append) {
                        is LoadState.Loading -> CircularProgressIndicator()
                        is LoadState.Error -> Text("Error Loading More Data")
                        else -> {}
                    }
                }
            }
        }
    }

}

@Composable
fun UserItem(user: User, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable {
                val userJson = Uri.encode(Gson().toJson(user))
                navController.navigate("details/$userJson")
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp) // Square aspect ratio
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${user.firstName} ${user.lastName}",
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                style = MaterialTheme.typography.headlineLarge,

            )
        }
    }
}
