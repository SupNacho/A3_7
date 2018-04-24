package ru.geekbrains.android3_7.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.repo.UsersRepo;

@Module
public class TestRepoModule
{


    @Provides
    public UsersRepo usersRepo()
    {
        User user = new User("SupNacho", null);
        UsersRepo repo =  Mockito.mock(UsersRepo.class);
        Mockito.when(repo.getUser("SupNacho")).thenReturn(Observable.just(user));
        return repo;
    }
}
