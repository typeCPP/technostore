package com.technostore.feature_shopping_cart.di

import com.technostore.feature_shopping_cart.business.ShoppingCartRepository
import com.technostore.feature_shopping_cart.business.ShoppingCartRepositoryImpl
import com.technostore.feature_shopping_cart.business.model.mapper.OrderDetailMapper
import com.technostore.feature_shopping_cart.business.model.mapper.ProductOrderMapper
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartEffectHandler
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartReducer
import com.technostore.network.service.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ShoppingCartModule {

    @Provides
    fun provideProductOrderMapper(): ProductOrderMapper {
        return ProductOrderMapper()
    }

    @Provides
    fun provideOrderDetailMapper(productOrderMapper: ProductOrderMapper): OrderDetailMapper {
        return OrderDetailMapper(productOrderMapper)
    }

    @Provides
    fun provideShoppingCartRepository(
        orderService: OrderService,
        orderDetailMapper: OrderDetailMapper
    ): ShoppingCartRepository {
        return ShoppingCartRepositoryImpl(orderService, orderDetailMapper)
    }

    /* Shopping cart */
    @Provides
    fun provideShoppingCartEffectHandler(shoppingCartRepository: ShoppingCartRepository): ShoppingCartEffectHandler {
        return ShoppingCartEffectHandler(shoppingCartRepository)
    }

    @Provides
    fun provideShoppingCartReducer(): ShoppingCartReducer {
        return ShoppingCartReducer()
    }
}