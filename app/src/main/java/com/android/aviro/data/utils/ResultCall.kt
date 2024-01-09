package com.android.aviro.data.utils

import android.util.Log
import com.android.aviro.data.entity.base.BaseResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException

class ResultCall<T>(private val call: Call<T>, private val retrofit: Retrofit) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {

                if (response.isSuccessful) {
                    Log.d("message","${response.body()}")

                    if (response.body() == null) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                Result.failure(
                                    IllegalStateException(
                                        "body가 비었습니다.",
                                        HttpException(response)
                                    )
                                )
                            )
                        )
                    } else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(response.code(), Result.success(response.body()!!))
                        )
                    }

                } else {

                    if (response.errorBody() == null) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                Result.failure(
                                    IllegalStateException(
                                        "errorBody가 비었습니다.",
                                        HttpException(response)
                                    )
                                )
                            )
                        )
                    } else {
                        val errorBody = retrofit.responseBodyConverter<BaseResponse>(
                            BaseResponse::class.java,
                            BaseResponse::class.java.annotations
                        ).convert(response.errorBody()!!)

                        val message: String = errorBody?.message ?: "errorBody가 비었습니다"

                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                Result.failure(
                                    IllegalStateException(
                                        message,
                                        HttpException(response)
                                    )
                                )
                            )
                        )

                    }

                }
            }


            override fun onFailure(call: Call<T>, t: Throwable) {
                val message = when (t) {
                    is IOException -> "인터넷 연결이 끊겼습니다."
                    is HttpException -> "알 수 없는 오류가 발생했어요."
                    else -> t.localizedMessage
                }
                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(IllegalStateException(message, t)))
                )

            }
        })
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(call.execute().body()!!))
    }

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(call.clone(), retrofit)
    }

    override fun request(): Request {
        return call.request()
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}