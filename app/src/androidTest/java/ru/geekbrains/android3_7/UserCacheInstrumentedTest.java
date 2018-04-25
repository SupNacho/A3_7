package ru.geekbrains.android3_7;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.inject.Inject;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.geekbrains.android3_7.di.DaggerTestComponent;
import ru.geekbrains.android3_7.di.TestComponent;
import ru.geekbrains.android3_7.di.modules.CacheModule;
import ru.geekbrains.android3_7.model.cache.AACache;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.cache.RealmCache;
import ru.geekbrains.android3_7.model.entity.User;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserCacheInstrumentedTest
{
    private static Realm testRealm;

    @Inject
    ICache realmCache;

    @Inject
    ICache aACache;

    @Test
    public void useAppContext()
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("ru.geekbrains.android3_7", appContext.getPackageName());
    }

    @BeforeClass
    public static void setupClass()
    {
        Realm.init(InstrumentationRegistry.getTargetContext());
        RealmConfiguration testConfig = new RealmConfiguration.Builder()
                .inMemory()
                .name("testRealm")
                .build();
        testRealm = Realm.getInstance(testConfig);
        Configuration dbConfiguration = new Configuration
                .Builder(InstrumentationRegistry.getTargetContext())
                .setDatabaseName("test.db").create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    @AfterClass
    public static void tearDownClass()
    {
        testRealm.close();
    }

    @Before
    public void setup()
    {

        TestComponent realmComponent = DaggerTestComponent
                .builder()
                .cacheModule(new CacheModule(){
                    @Override
                    public ICache cacheType() {
                        return new RealmCache(testRealm, true);
                    }
                })
                .build();

        realmComponent.inject(this);

        TestComponent aaComponent = DaggerTestComponent
                .builder()
                .cacheModule(new CacheModule(){
                    @Override
                    public ICache cacheType() {
                        return new AACache();
                    }
                }).build();

        aaComponent.inject(this);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void putAndGetUserInRealm()
    {
        realmCache.putUser(new User("testLogin2","testUrl2"));
        realmCache.getUser("testLogin2").subscribe(u -> {
            assertEquals("testLogin2", u.getLogin());
            assertEquals("testUrl2", u.getAvatarUrl());
        });
    }
    @Test
    public void putAndGetUserInAA()
    {
        aACache.putUser(new User("testLoginAAAA","testUrlAA"));
        aACache.getUser("testLoginAAAA").subscribe(u -> {
            assertEquals("testLoginAAAA", u.getLogin());
            assertEquals("testUrlAA", u.getAvatarUrl());
        });
    }
}
