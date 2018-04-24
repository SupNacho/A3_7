package ru.geekbrains.android3_7.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.android3_7.model.api.ApiService;

@Singleton
@Module
public class ApiModule
{
    @Provides
    public ApiService api(Retrofit retrofit)
    {
        return retrofit.create(ApiService.class);
    }

    @Named("endpoint")
    @Provides
    public String endpoint(){
        return "https://api.github.com/";
    }

    @Provides
    public Retrofit retrofit(@Named("endpoint") String endpoint, GsonConverterFactory gsonConverterFactory)
    {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson)
    {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public Gson gson()
    {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

}
