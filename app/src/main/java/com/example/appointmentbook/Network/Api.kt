package com.example.appointmentbook.Network

import com.example.appointmentbook.data.ActionStatusData
import com.example.appointmentbook.data.AlllBookReq.AllBookReqDataItem
import com.example.appointmentbook.data.BookSlotData
import com.example.appointmentbook.data.DoctorListData.DoctorsListData
import com.example.appointmentbook.data.DoctorSlotsReq.DoctorSlotsReqItem
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
    @POST("register")
    suspend fun signUpStudent(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") branch: String,
    ): Response<Signup>

    @FormUrlEncoded
    @POST("teacher/register")
    suspend fun signUpTeacher(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpTeacher>

    @GET("my_requests")
    suspend fun bookReq(@Header("Authorization") BearerToken: String): Response<List<AllBookReqDataItem>>

    @GET("doctor/{id}/slots")
    suspend fun slotAvailable(
        @Header("Authorization") BearerToken: String,
        @Path("id") id: Int
    ): Response<List<SlotsData>>

    @POST("doctor/{slot_id}/book")
    suspend fun bookSlot(
        @Header("Authorization") token: String,
        @Path("slot_id") id: Int
    ): Response<BookSlotData>

    @GET("my_requests/pending")
    suspend fun slotReqPending(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @GET("my_requests/accepted")
    suspend fun slotReqAccepted(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @GET("my_requests/rejected")
    suspend fun slotReqRejected(@Header("Authorization") BearerToken: String): Response<List<SlotBookRequestsItem>>

    @GET("slots/requests")
    suspend fun allSlotReq(@Header("Authorization") BearerToken: String): Response<List<AllBookReqDataItem>>

    @GET("slots/my")
    suspend fun docReqList(@Header("Authorization") BearerToken: String): Response<List<DoctorSlotsReqItem>>

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
        .baseUrl("https://6f2018af407d.ngrok.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(ApiClient::class.java)
}