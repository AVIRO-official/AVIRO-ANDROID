package com.android.aviro.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.datasource.member.MemberDataSource
import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse
import com.android.aviro.domain.entity.MemberEntity
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject


class MemberRepositoryImp @Inject constructor (
    private val memberDataSource : MemberDataSource,
    private val dataStoreDataSource : DataStoreDataSource
) : MemberRepository {


    override suspend fun getMemberInfoFromLocal(key : String): String? {
        return dataStoreDataSource.readDataStore(key)
    }

    override suspend fun saveMemberInfoToLocal(user_id : Int, user_name : String, user_email : String, nickname : String) {
        dataStoreDataSource.writeDataStore("user_id", user_id.toString())
        dataStoreDataSource.writeDataStore("user_name",user_name)
        dataStoreDataSource.writeDataStore("user_email", user_email)
        dataStoreDataSource.writeDataStore("nickname",nickname)
    }

    override suspend fun creatMember(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : Result<Any> {

        val access_token = dataStoreDataSource.readDataStore("access_token").toString()
        val refresh_token = dataStoreDataSource.readDataStore("refresh_token").toString()
        val user_id = dataStoreDataSource.readDataStore("user_id")!!.toInt()
        val user_name = dataStoreDataSource.readDataStore("user_name").toString()
        val user_email = dataStoreDataSource.readDataStore("user_email").toString()

        val new_member = MemberEntity(access_token, refresh_token, user_id, user_name, user_email, nickname, birth, gender, marketingAgree = true)
        return memberDataSource.creatMember(new_member)
    }

    suspend override fun checkNickname(nickname : NicknameEntity) : Result<NicknameCheckResponse> {
        return memberDataSource.checkNickname(nickname)
    }


}