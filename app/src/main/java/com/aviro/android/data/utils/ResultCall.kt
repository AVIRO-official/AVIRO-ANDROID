package com.aviro.android.data.utils

import android.util.Log
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap


// 네트워크 응답 에러
class ResultCall<T>(private val call: Call<T>, private val retrofit: Retrofit, private val requestMap: ConcurrentHashMap<String, Call<*>>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        val requestKey = "${call.request().url}"

        if (requestMap.putIfAbsent(requestKey, call) == null) {

                call.enqueue(object : Callback<T> {
                    // 서버로부터 응답이 정상적으로 들어왔을때
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        Log.d("ResultCall","${response}")
                        if (response.isSuccessful) { // 2xx
                            val code = response.code()

                            when (code) {
                                200 -> {
                                    // 200
                                    callback.onResponse(
                                        this@ResultCall,
                                        Response.success(Result.success(response.body()!!))
                                    )
                                    // 200이지만 서버에서 주는 코드에 따라 또 다름
                                }

                                204 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 서버로부터 정보를 가져오지 못했습니다.\n앱을 다시 실행해주세요.")))
                                )
                            }

                        } else { // 3xx,4xx,5xx
                            val code = response.code()

                            when (code) {
                                in 300..399 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 서버로부터 정보를 가져오지 못했습니다.\n앱을 다시 실행해주세요.")))
                                )

                                in 400..403 -> callback.onResponse(
                                        this@ResultCall,
                                        Response.success(Result.failure(Exception("[${code}] 요청 권한이 없습니다.\n로그아웃 후 다시 시도해주세요.\n문제가 지속될 경우 문의사항에 문의해주세요.")))
                                    )

                                404 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 서버에서 요청하신 정보를 찾을 수 없습니다.\n" +
                                            "문제가 지속될 경우 문의사항에 문의해주세요.")))
                                )
                                408 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 요청 시간을 초과했습니다.\n다시 시도해주세요.")))
                                )
                                in 409..499 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 잘못된 요청입니다.\n다시 시도해주세요.")))

                                )
                                in 500..599 -> callback.onResponse(
                                    this@ResultCall,
                                    Response.success(Result.failure(Exception("[${code}] 서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")))
                                )

                                else -> {
                                    callback.onResponse(
                                        this@ResultCall,
                                        Response.success(Result.failure(Exception("알 수 없는 오류가 발생했어요.\n다시 시도해주세요.")))

                                    )
                                }
                            }

                        }
                        requestMap.remove(requestKey)
                    }


                    // 서버 통신 중 에러가 나서 제대로 된 응답조차 받지 못한 상황 (예외처리 하는게 아니라 success로 감싸서 안내를 띄운다)
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        val message = when (t) {
                            is IOException -> "인터넷 연결을 확인해주세요."
                            is HttpException -> "알 수 없는 오류가 발생했어요."
                            else -> t.localizedMessage
                        }
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(IllegalStateException(message, t)))
                        )
                        requestMap.remove(requestKey)
                    }
                })
            } else {
                cancel()
                return
           }

    }



    override fun isExecuted(): Boolean {
        return call.isExecuted
    }


    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(call.execute().body()!!))
    }

    override fun cancel() {
        Log.d("cancel","작업 취소")
        call.cancel()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(call.clone(), retrofit, requestMap)
    }

    override fun request(): Request {
        return call.request()
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}