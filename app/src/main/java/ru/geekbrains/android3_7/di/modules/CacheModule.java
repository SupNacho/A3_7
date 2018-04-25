package ru.geekbrains.android3_7.di.modules;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.cache.RealmCache;

@Singleton
@Module
public class CacheModule
{
    @Named(value = "cache")
    @Provides
    public ICache cacheType(){
        return new RealmCache(Realm.getDefaultInstance(), false);
    }

    @Provides
    public ICache cache(@Named("cache") ICache cache)
    {
        return cache;
    }
}
