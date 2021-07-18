package com.example.appointmentbook.Network

import com.example.appointmentbook.data.AlllBookReq.AllBookReqDataItem
import com.example.appointmentbook.data.BookReqData.BookReqDataItem
import com.example.appointmentbook.data.BookSlotData
import com.example.appointmentbook.data.CreateSlotData
import com.example.appointmentbook.data.DoctorListData.DoctorsListData
import com.example.appointmentbook.data.SlotActionData
import com.example.appointmentbook.data.SlotBookRequests.SlotBookRequestsItem
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.data.SlotsbyReqIdData.SlotsByReqIdItem
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
    suspend fun docSlotAvailable(
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


//    @FormUrlEncoded
//    @PUT("slot/action")
//    suspend fun slotAction(
//        @Header("Authorization") BearerToken: String,
//        @Field("status") status: String,
//        @Field("request_id") requestId: Int
//    ): Response<ActionStatusData>

    @GET("doctors")
    suspend fun doctorList(@Header("Authorization") BearerToken: String): Response<List<DoctorsListData>>

    @GET("slots/my")
    suspend fun docSlotRec(@Header("Authorization") BearerToken: String): Response<List<BookReqDataItem>>

    @FormUrlEncoded
    @POST("slots/action/{action}")
    suspend fun slotAction(
        @Header("Authorization") BearerToken: String,
        @Path("action") action: String,
        @Field("request_id") request_id: Int,
    ): Response<SlotActionData>

    @GET("slots/{id}/requests")
    suspend fun slotsByID(
        @Header("Authorization") BearerToken: String,
        @Path("id") slot_id: Int,
    ): Response<List<SlotsByReqIdItem>>

    @FormUrlEncoded
    @POST("slots/add")
    suspend fun createSlots(
        @Header("Authorization") BearerToken: String,
        @Field("start_time") startTime: String,
        @Field("end_time") endTime: String,
        @Field("booking_start_time") bookingStartTime: String,
        @Field("booking_end_time") bookingEndTime: String,
        @Field("capacity") slotCapacity: Int,
        @Field("available") available: Boolean,
    ): Response<CreateSlotData>

}

object ApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://77d817118110.ngrok.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(ApiClient::class.java)
}