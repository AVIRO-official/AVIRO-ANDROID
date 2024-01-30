package com.android.aviro.data.utils

import android.util.Log
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.net.UnknownHostException

// 네트워크 응답 에러
class ResultCall<T>(private val call: Call<T>, private val retrofit: Retrofit) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        call.enqueue(object : Callback<T> {

            // 서버로부터 응답이 정상적으로 들어왔을때
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) { // 2xx
                    val code = response.code()

                    when(code){
                        200 -> {
                            // 200
                            callback.onResponse(
                                this@ResultCall,Response.success(Result.success(response.body()!!)))
                        }

                        204 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("서버로부터 정보를 가져오지 못했습니다.\n앱을 다시 실행해주세요."))))
                    }
                    /*
                    if (response.body() == null) {

                        // 2xx이지만 내용이 비었음
                        callback.onResponse(
                            this@ResultCall,
                                    Response.success(Result.failure(IllegalStateException("${response.code()} + 서버로부터 정보를 받지 못했습니다. 다시 시도해주세요.")))
                        )
                    } else {
                        // 네트워크 2xx 이여도 안의 내용이 4xx,3xx,5xx 인 경우에는 팝업으로 알림
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(response.code(), Result.success(response.body()!!))
                        )
                    }

                     */

                } else { // 3xx,4xx,5xx
                    val code = response.code()
                    when(code) {
                        in 300..399 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("서버로부터 정보를 가져오지 못했습니다.\n앱을 다시 실행해주세요."))))
                        400 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("청"))))
                        401 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("요청 권한이 없습니다.\n로그아웃 후 다시 로그인하여 재시도해주세요."))))
                        404 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("서버에서 요청하신 정보를 찾을 수 없습니다.\n서버로부터 정보를 확인해주세요."))))
                        408 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("요청 시간을 초과했습니다.\n다시 시도해주세요."))))
                        in 409..499 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("잘못된 요청입니다.\n다시 시도해주세요."))))
                        in 500..599 -> callback.onResponse(
                            this@ResultCall,Response.success(Result.failure(Exception("서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요."))))
                    }
                    /*
                    if (response.errorBody() == null) { // errorBody가 없어서 어떤 에러인지 알 수 없을 때
                        val code = response.code()
                        var message = ""
                        when(code) {
                            300 -> message = ""


                        }
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(Exception(message)))
                            //Response.success(response.code(), Result.failure(response.errorBody()!!))
                            //Response.success(Result.failure(Exception("${response.code()} : 알 수 없는 에러가 발생했습니다.")))
                        )

                    } else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(response.code(), Result.failure(Exception(response.message())))
                            //Response.success(Result.failure(Exception("${response.code()} :" + response.message())))
                        )

                    }

                     */

                }
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