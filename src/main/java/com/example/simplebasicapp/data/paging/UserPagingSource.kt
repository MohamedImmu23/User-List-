package com.example.simplebasicapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.simplebasicapp.data.model.User
import com.example.simplebasicapp.data.repository.UserRepository
import kotlinx.coroutines.delay

class UserPagingSource(private val repository: UserRepository, private val searchQuery: String) :
    PagingSource<Int, User>() {

    companion object {
        private const val INITIAL_PAGE = 1
        private const val LOAD_DELAY = 500L
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: INITIAL_PAGE
        return try {
            delay(LOAD_DELAY)
            val response = repository.fetchUsers(page, searchQuery)

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = response.takeIf { it.isNotEmpty() }?.let { page + 1 }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }
    }
}