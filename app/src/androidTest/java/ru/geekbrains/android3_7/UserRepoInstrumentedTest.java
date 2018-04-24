package ru.geekbrains.android3_7;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.geekbrains.android3_7.di.DaggerTestComponent;
import ru.geekbrains.android3_7.di.TestComponent;
import ru.geekbrains.android3_7.di.modules.ApiModule;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.repo.UsersRepo;
import ru.geekbrains.android3_7.presenter.MainPresenter;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserRepoInstrumentedTest
{
    private static MockWebServer webServer;

    @Inject
    UsersRepo usersRepo;

    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("ru.geekbrains.android3_7", appContext.getPackageName());
    }

    @BeforeClass
    public static void setupClass() throws IOException
    {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException
    {
        webServer.shutdown();
    }

    @Before
    public void setup()
    {
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule(){
                    @Override
                    public String endpoint()
                    {
                        return webServer.url("/").toString();
                    }
                })
                .build();

        component.inject(this);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void getUser()
    {
        webServer.enqueue(createUserResponse("somelogin", "someurl"));
        TestObserver<User> observer = new TestObserver<>();
        usersRepo.getUser("somelogin").subscribe(observer);
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(), "somelogin");
        assertEquals(observer.values().get(0).getAvatarUrl(), "someurl");
    }

    private MockResponse createUserResponse(String login, String avatar)
    {
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatar +"\"}";
        return new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(body);
    }
}
