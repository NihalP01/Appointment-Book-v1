package com.example.appointmentbook.Network

import com.example.appointmentbook.data.ActionStatusData
import com.example.appointmentbook.data.BookRequestData.BookRequestData
import com.example.appointmentbook.data.BookSlotData
import com.example.appointmentbook.data.DoctorListData.DoctorsListData
import com.example.appointmentbook.data.SlotBookRequests.SlotBookRequestsItem
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.data.roleData.RoleData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiClient {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("id") email: String,
        @Field("password") password: String
    ): Response<Login>

    @GET("me")
    suspend fun role(@Header("Authorization") BearerToken: String): Response<RoleData>

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

    @GET("my_requests/pending")
    suspend fun slotReqPending(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @GET("my_requests/accepted")
    suspend fun slotReqAccepted(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @GET("my_requests/rejected")
    suspend fun slotReqRejected(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @FormUrlEncoded
    @PUT("slot/action")
    suspend fun slotAction(
        @Header("Authorization") BearerToken: String,
        @Field("status") status: String,
        @Field("request_id") requestId: Int
    ): Response<ActionStatusData>

    @GET("doctors")
    suspend fun doctorList(@Header("Authorization") BearerToken: String): Response<List<DoctorsListData>>
}

object ApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://fe10879e9f21.ngrok.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(ApiClient::class.java)
}