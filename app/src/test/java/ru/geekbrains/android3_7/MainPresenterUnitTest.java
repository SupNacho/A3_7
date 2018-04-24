package ru.geekbrains.android3_7;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.TestScheduler;

import ru.geekbrains.android3_7.di.DaggerTestComponent;
import ru.geekbrains.android3_7.di.TestComponent;
import ru.geekbrains.android3_7.di.modules.TestRepoModule;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.repo.UsersRepo;
import ru.geekbrains.android3_7.presenter.MainPresenter;
import ru.geekbrains.android3_7.view.MainView;

public class MainPresenterUnitTest
{
    private MainPresenter mainPresenter;
    private TestScheduler testScheduler;

    @Mock MainView mainView;

    @BeforeClass
    public static void setupClass()
    {

    }

    @AfterClass
    public static void tearDownClass() { }


    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        mainPresenter = Mockito.spy(new MainPresenter(testScheduler));
        TestComponent component = DaggerTestComponent.builder().testRepoModule(new TestRepoModule()).build();
        component.inject(mainPresenter);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void onFirstViewAttach()
    {
        mainPresenter.attachView(mainView);
        Mockito.verify(mainView).init();
    }

    @Test
    public void onPermissionsGranted()
    {
        mainPresenter.onPermissionsGranted();
        Mockito.verify(mainPresenter).loadInfo();
    }

    @Test
    public void loadInfo()
    {
        loadInfoSuccess();
        loadInfoError();
    }

    private void loadInfoSuccess()
    {
        mainPresenter.loadInfo();

        testScheduler.advanceTimeBy(1 , TimeUnit.SECONDS);

        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).showAvatar(null);
        Mockito.verify(mainView).setUsername("SupNacho");
        Mockito.verify(mainView).updateRepoList();
    }

    private void loadInfoError()
    {
        testScheduler.advanceTimeBy(1 , TimeUnit.SECONDS);
        TestComponent component = DaggerTestComponent.builder().testRepoModule(new TestRepoModule(){
            @Override
            public UsersRepo usersRepo()
            {
                return super.usersRepo();
            }
        }).build();
        component.inject(mainPresenter);
        mainPresenter.loadInfo();
        Mockito.verify(mainView).hideLoading();
    }

}