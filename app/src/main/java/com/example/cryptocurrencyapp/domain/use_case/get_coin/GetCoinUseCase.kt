package com.example.cryptocurrencyapp.domain.use_case.get_coin

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.data.remote.dto.CoinDetailDto
import com.example.cryptocurrencyapp.data.remote.dto.toCoin
import com.example.cryptocurrencyapp.data.remote.dto.toCoinDetail
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.model.CoinDetail
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    operator fun invoke(coinId : String): Flow<Resource<CoinDetail>> = flow {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                Build.VERSION_CODES.S) >= 7) {
            try {
                emit(Resource.Loading<CoinDetail>())
                val coin = repository.getCoinById(coinId).toCoinDetail()
                emit(Resource.Success<CoinDetail>(coin))
            }
            catch (e: HttpException){
                emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "An unexpected error occurred"))
            }
            catch (e: IOException){
                emit(Resource.Error<CoinDetail>("could not reach server. check your internet connection"))
            }
        }
    }

}