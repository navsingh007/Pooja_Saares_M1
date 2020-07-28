package com.seasia.poojasarees.api;

import com.google.gson.JsonObject;
import com.seasia.poojasarees.model.AddressIn;
import com.seasia.poojasarees.model.Addresses;
import com.seasia.poojasarees.model.request.AdminIn;
import com.seasia.poojasarees.model.request.SignUpIn;
import com.seasia.poojasarees.model.request.UpdateProfileIn;
import com.seasia.poojasarees.model.request.cart.AddToCartIn;
import com.seasia.poojasarees.model.AddressOut;
import com.seasia.poojasarees.model.request.shipping.PaymentInfoIn;
import com.seasia.poojasarees.model.request.shipping.ShippingInfoIn;
import com.seasia.poojasarees.model.response.AllStatesOut;
import com.seasia.poojasarees.model.response.AllTownsOut;
import com.seasia.poojasarees.model.response.cart.AddToCartOut;
import com.seasia.poojasarees.model.response.cart.CustomerByCartIdOut;
import com.seasia.poojasarees.model.response.category.CategoryListOut;
import com.seasia.poojasarees.model.response.home.HomeOut;
import com.seasia.poojasarees.model.response.authentication.LoginOut;
import com.seasia.poojasarees.model.response.authentication.OtpOut;
import com.seasia.poojasarees.model.response.orderpayment.PlaceOrderIn;
import com.seasia.poojasarees.model.response.products.ProductDetailsOut;
import com.seasia.poojasarees.model.response.products.ProductFilterAttributes;
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut;
import com.seasia.poojasarees.model.response.profile.ProfileOut;
import com.seasia.poojasarees.model.response.profile.ProfilePicOut;
import com.seasia.poojasarees.model.response.authentication.SignUpOut;
import com.seasia.poojasarees.model.response.authentication.SignupPhoneNoOut;
import com.seasia.poojasarees.model.response.shipping.AvailableShippingMethodOut;
import com.seasia.poojasarees.model.response.shipping.SetShippingAndBillingAddressOut;
import com.seasia.poojasarees.model.response.wishlist.AllWishlistProductsOut;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("integration/admin/token")
    Call<String> getAdminToken(@Body AdminIn adminIn);

    @POST("customers")
    Call<SignUpOut> signUpUser(@Body SignUpIn signUpIn);

    @POST("validateByPhone")
    Call<LoginOut> loginUser(@Body JsonObject loginIn);

    @POST("sendOtp")
    Call<OtpOut> sendOtp(@Body JsonObject otpIn);

    @POST("verifyOtp")
    Call<OtpOut> verifyOtp(@Body JsonObject otpIn);

    @POST("setPasswordByPhone")
    Call<OtpOut> changePassword(@Body JsonObject otpIn);

    @POST("getHomeContent")
    Call<HomeOut> getHomeContent(@Body JsonObject homeIn);


    // User Profile
    @PUT("customers/{id}")
    Call<ProfileOut> updateProfile(@Path("id") String customerId, @Body UpdateProfileIn updateProfileIn);

    @GET("customers/{id}")
    Call<ProfileOut> getProfile(@Path("id") String customerId);

    @GET("customers/me")
    Call<ProfileOut> getProfileMe();

    @GET("directory/countries")
    Call<ArrayList<AllStatesOut>> getAllStates();

    @POST("profilePic")
    Call<ProfilePicOut> updateProfilePic(@Body JsonObject profilePicIn);

//    @POST("profilePic")
//    Call<ProfilePicOut> updateProfilePic(@PartMap HashMap<String, RequestBody> mHashMap,
//                                         @Part MultipartBody.Part image);

    @POST("getCustomerByPhone")
    Call<SignupPhoneNoOut> uniquePhoneNoCheck(@Body JsonObject phoneNoIn);


    // ****************** Milestone - 2 *********************//

    // Address
    @PUT("customers/{id}")
    Call<AddressOut> addOrUpdateAddress(@Path("id") String customerId, @Body AddressIn addressIn);

    @GET("customers/addresses/{id}")
    Call<Addresses> getAddressById(@Path("id") String addressId);

    @DELETE("addresses/{id}")
    Call<Boolean> deleteAddressById(@Path("id") String addressId);



    @GET("cityOptions")
    Call<ArrayList<AllTownsOut>> getAllTowns();

    // Category Listing
    @GET("categories")
    Call<CategoryListOut> getCategoryListing();

    // Product details by ID
    @GET("products?searchCriteria[filterGroups][0][filters][0][field]=entity_id&" +
            "searchCriteria[filterGroups][0][filters][0][condition_type]=eq&")
    Call<ProductDetailsOut> getProductById(@Query("searchCriteria[filterGroups][0][filters][0][value]") String productId);


    // Get products by category ID
//    @GET("products?searchCriteria[filterGroups][0][filters][0][field]=category_id& " +
//            "searchCriteria[filterGroups][0][filters][0][value]=4&" +
//            " searchCriteria[filterGroups][0][filters][0][conditionType]=eq&" +
//            "searchCriteria[filterGroups][0][filters][1][field]=visibility& " +
//            "searchCriteria[filterGroups][0][filters][1][value]=4& " +
//            "searchCriteria[filterGroups][0][filters][0][conditionType]=eq&" +
//            "searchCriteria[sortOrders][0][field]=created_at& " +
//            "searchCriteria[sortOrders][0][direction]=DESC& " +
//            "searchCriteria[pageSize]=10& searchCriteria[currentPage]=1")
//    Call<ProductsByCategoryIdOut> getProductsByCategoryId();


    @GET("products")
    Call<ProductsByCategoryIdOut> getProductsByCategoryId(@Query("searchCriteria[filterGroups][0][filters][0][field]") String categoryId,
                                                          @Query("searchCriteria[filterGroups][0][filters][0][value]") String catIdValue,
                                                          @Query("searchCriteria[filterGroups][0][filters][0][conditionType]") String catIdCondition,
                                                          @Query("searchCriteria[filterGroups][0][filters][1][field]") String visibility,
                                                          @Query("searchCriteria[filterGroups][0][filters][1][value]") String visibilityValue,
                                                          @Query("searchCriteria[filterGroups][0][filters][1][conditionType]") String visibilityCondition,
                                                          @Query("searchCriteria[sortOrders][0][field]") String createdAt,
                                                          @Query("searchCriteria[sortOrders][0][direction]") String sortType,
                                                          @Query("searchCriteria[pageSize]") String pageSize,
                                                          @Query("searchCriteria[currentPage]") String currentPage
    );

    @POST("getFilterAttributes")
    Call<ProductFilterAttributes> getFiltersAttributes(@Body JsonObject filterAttributesIn);


    // WISHLIST management

    @POST("addWishlistItems")
    Call<Boolean> addProductToWishlist(@Body JsonObject addWishlistIn);

    @POST("deleteWishlistItem")
    Call<Boolean> deleteProductFromWishlist(@Body JsonObject deleteWishlistIn);

    @POST("wishlistItems")
    Call<ArrayList<AllWishlistProductsOut>> getWishlistItems(@Body JsonObject wishlistItemsIn);


    // CART management

    @POST("carts/mine")
    Call<Integer> createCustCart();

    @POST("carts/mine/items")
    Call<AddToCartOut> addItemsToCart(@Body AddToCartIn addToCartIn);

    @GET("carts/{id}")
    Call<CustomerByCartIdOut> getCustCartById(@Path("id") String cartId);

    /**
     * Below API's not tested, and response model may need to be updated
     */
    @DELETE("carts/{id}/items/{itemId}")
    Call<Boolean> deleteItemFromCart(@Path("id") String cartId, @Path("itemId") String itemId);

    // Shipping and Billing
    @POST("carts/mine/estimate-shipping-methods-by-address-id")
    Call<ArrayList<AvailableShippingMethodOut>> getAvailableShippingMethodsByAddressId(@Body JsonObject shippingMethodsIn);

    @POST("carts/mine/shipping-information")
    Call<SetShippingAndBillingAddressOut> setShippingAndBillingAddressForCart(@Body ShippingInfoIn shippingInfoIn);

    @GET("carts/mine/payment-information")
    Call<SetShippingAndBillingAddressOut> selectPaymentMethodForOrder(@Body PaymentInfoIn paymentInfoIn);

    @PUT("carts/mine/order")
    Call<String> placeOrder(@Body PlaceOrderIn placeOrderIn);
}