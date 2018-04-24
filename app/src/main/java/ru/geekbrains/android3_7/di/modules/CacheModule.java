package ru.geekbrains.android3_7.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.cache.RealmCache;

@Singleton
@Module
public class CacheModule
{
    @Provides
    public ICache cache()
    {
        return new RealmCache();
    }
}
