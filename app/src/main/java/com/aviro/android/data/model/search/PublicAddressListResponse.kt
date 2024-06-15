package com.aviro.android.data.model.search

data class PublicAddressListResponse(
    val results : Results
)

data class Results(
    val common : ResultStatus,
    val juso : List<Juso>
)

data class ResultStatus(
    val totalCount : String,
    val currentPage : String,
    val countPerPage : String,
    val errorCode : String,
    val errorMessage : String,
)

data class Juso(
    val roadAddr : String,
    val roadAddrPart1 : String,
    val roadAddrPart2 : String,
    val engAddr : String,
    val jibunAddr : String,
    val zipNo : String,
    val admCd : String,
    val rnMgtSn : String,
    val bdMgtSn : String,
    val detBdNmList : String,
    val bdNm : String,
    val bdKdcd : String,
    val siNm : String,
    val emdNm : String,
    val liNm : String,
    val rn : String,
    val udrtYn : String,
    val buldMnnm : String,
    val buldSlno : String,
    val mtYn : String,
    val lnbrMnnm : String,
    val lnbrSlno : String,
    val emdNo : String,
    val hstryYn : String,
    val relJibun : String,
    val hemdNm : String,
)

