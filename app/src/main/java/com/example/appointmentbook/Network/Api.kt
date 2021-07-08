package com.example.appointmentbook.Network

import com.example.appointmentbook.data.ActionStatusData
import com.example.appointmentbook.data.BookRequestData.BookRequestData
import com.example.appointmentbook.data.BookSlotData
import com.example.appointmentbook.data.RoleDataClass.roleData
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.data.sample.SlotPendingDataItem
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiClient {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Login>

    @GET("me")
    suspend fun role(@Header("Authorization") BearerToken: String): Response<roleData>

    @FormUrlEncoded
    @POST("student/register")
    suspend fun signUpStudent(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("branch") branch: String,
        @Field("semester") semester: String,
    ): Response<Signup>

    @FormUrlEncoded
    @POST("teacher/register")
    suspend fun signUpTeacher(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpTeacher>

    @GET("my_requests")
    suspend fun bookReq(@Header("Authorization") BearerToken: String): Response<BookRequestData>

    @GET("slot/available")
    suspend fun slotAvailable(): Response<List<SlotsData>>

    @FormUrlEncoded
    @POST("slot/request")
    suspend fun bookSlot(
        @Header("Authorization") token: String,
        @Field("teacher_slot_id") id: Int
    ): Response<BookSlotData>

    @GET("slot/requests/pending")
    suspend fun slotReqPending(@Header("Authorization") BearerToken: String): Response<List<SlotPendingDataItem>>

    @GET("slot/requests/accepted")
    suspend fun slotReqAccepted(@Header("Authorization") BearerToken: String): Response<List<SlotPendingDataItem>>

    @GET("slot/requests/rejected")
    suspend fun slotReqRejected(@Header("Authorization") BearerToken: String): Response<List<SlotPendingDataItem>>

    @FormUrlEncoded
    @PUT("slot/action")
    suspend fun slotAction(
        @Header("Authorization") BearerToken: String,
        @Field("status") status: String,
        @Field("request_id") requestId: Int
    ): Response<ActionStatusData>

}

object ApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("http://tbook.techmess.in/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(ApiClient::class.java)
}