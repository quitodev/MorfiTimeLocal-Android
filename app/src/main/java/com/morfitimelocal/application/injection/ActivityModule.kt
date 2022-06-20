package com.morfitimelocal.application.injection

import com.morfitimelocal.domain.place.account.AccountImpl
import com.morfitimelocal.domain.place.account.IAccountRepo
import com.morfitimelocal.domain.place.sector.SectorImpl
import com.morfitimelocal.domain.place.sector.ISectorRepo
import com.morfitimelocal.domain.login.ILoginRepo
import com.morfitimelocal.domain.login.LoginImpl
import com.morfitimelocal.domain.IMainRepo
import com.morfitimelocal.domain.MainImpl
import com.morfitimelocal.domain.place.booking.BookingImpl
import com.morfitimelocal.domain.place.booking.IBookingRepo
import com.morfitimelocal.domain.place.home.HomeImpl
import com.morfitimelocal.domain.place.home.IHomeRepo
import com.morfitimelocal.domain.place.ubication.IUbicationRepo
import com.morfitimelocal.domain.place.ubication.UbicationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(ActivityRetainedComponent::class)
@ExperimentalCoroutinesApi
abstract class ActivityModule {
    @ActivityRetainedScoped
    @Binds
    abstract fun bindLoginDataSource(loginImpl: LoginImpl) : ILoginRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindMainDataSource(mainImpl: MainImpl) : IMainRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindHomeDataSource(homeImpl: HomeImpl) : IHomeRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindBookingDataSource(bookingImpl: BookingImpl) : IBookingRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindSectorDataSource(sectorImpl: SectorImpl) : ISectorRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindUbicationDataSource(ubicationImpl: UbicationImpl) : IUbicationRepo
    @ActivityRetainedScoped
    @Binds
    abstract fun bindAccountDataSource(accountImpl: AccountImpl) : IAccountRepo
}
