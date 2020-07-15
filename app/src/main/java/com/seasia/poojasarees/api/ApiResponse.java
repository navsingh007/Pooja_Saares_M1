package com.seasia.poojasarees.api;

/*
//* Created by Saira on 03/07/2019.
 */
import retrofit2.Response;

public interface ApiResponse<T> {
    void onResponse(Response<T> mResponse, String mKey);
    void onError(String mKey);
}
