package io.yatin.datatracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yatin on 04/02/18.
 */

public interface API {
    @GET("posts")
    Call<List<ResponseBody>> getPost();
}

