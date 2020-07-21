package com.seasia.poojasarees.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.seasia.poojasarees.api.ApiClient;
import com.seasia.poojasarees.api.ApiInterface;
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsByCatIdDataSource {
/*public class ProductsByCatIdDataSource extends PageKeyedDataSource<Integer, ProductsByCategoryIdOut> {

    private static final String TAG = ProductsByCatIdDataSource.class.getSimpleName();
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private ApiInterface apiInterface;

    public ProductsByCatIdDataSource() {
        this.apiInterface = ApiClient.getApiInterface();
        initialLoading = new MutableLiveData();
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    *//*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     *//*
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, ProductsByCategoryIdOut> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        apiInterface.getProductsByCategoryId("category_id",
                "4",
                "eq",
                "visibility",
                "4",
                "eq",
                "created_at",
                "DESC",
                1 + "",
                params.requestedLoadSize + "")
                .enqueue(new Callback<ProductsByCategoryIdOut>() {
                    @Override
                    public void onResponse(Call<ProductsByCategoryIdOut> call, Response<ProductsByCategoryIdOut> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().getItems(), null, 2);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsByCategoryIdOut> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, ProductsByCategoryIdOut> callback) {

    }

    *//*
     * Step 3: This method it is responsible for the subsequent call to load the data page wise.
     * This method is executed in the background thread
     * We are fetching the next page data from the api
     * and passing it via the callback method to the UI.
     * The "params.key" variable will have the updated value.
     *//*
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, ProductsByCategoryIdOut> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);

        apiInterface.getProductsByCategoryId("category_id",
                "4",
                "eq",
                "visibility",
                "4",
                "eq",
                "created_at",
                "DESC",
                params.key.toString(),
                params.requestedLoadSize + "").enqueue(new Callback<ProductsByCategoryIdOut>() {
            @Override
            public void onResponse(Call<ProductsByCategoryIdOut> call, Response<ProductsByCategoryIdOut> response) {
                *//*
                 * If the request is successful, then we will update the callback
                 * with the latest feed items and
                 * "params.key+1" -> set the next key for the next iteration.
                 *//*
                if (response.isSuccessful()) {
                    int nextKey = (params.key == response.body().getTotal_count()) ? null : params.key + 1;
                    callback.onResult(response.body().getItems(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<ProductsByCategoryIdOut> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }*/
}
