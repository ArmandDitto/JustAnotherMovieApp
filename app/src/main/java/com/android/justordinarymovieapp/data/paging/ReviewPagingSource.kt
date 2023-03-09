package com.android.justordinarymovieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.justordinarymovieapp.base.network.parseRetrofitException
import com.android.justordinarymovieapp.data.repository.MovieRepository
import com.android.justordinarymovieapp.model.review.Review
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val repository: MovieRepository,
    private val movieId: Int
) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val page = params.key ?: 1
        return try {
            val response = repository.getReviewPaging(movieId, page)

            val data = response.results ?: listOf()
            val prevKey = if (page == 1) null else page - 1
            val nextKey: Int? = if (data.isEmpty()) null else page.plus(1)

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(Throwable(message = "Cannot connect to server: Please make sure you are connected to the Internet and try again."))
        } catch (e: HttpException) {
            return LoadResult.Error(Throwable(message = parseRetrofitException(e)))
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}