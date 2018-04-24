package ru.geekbrains.android3_7.model.repo;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_7.NetworkStatus;
import ru.geekbrains.android3_7.model.api.ApiService;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.entity.Repository;
import ru.geekbrains.android3_7.model.entity.User;

public class UsersRepo
{

    ApiService api;
    ICache cache;

    public UsersRepo(ApiService api, ICache cache)
    {
        this.cache = cache;
        this.api = api;
    }

    public Observable<User> getUser(String username)
    {
        if (NetworkStatus.isOnline())
        {
            return api.getUser(username).map(user ->
            {
                cache.putUser(user);
                return user;
            });
        } else
        {
            return cache.getUser(username);
        }
    }

    public Observable<List<Repository>> getUserRepos(User user)
    {
        if (NetworkStatus.isOnline())
        {
            return api.getUserRepos(user.getLogin()).map(repos ->
            {
                cache.putUserRepos(user, repos);
                return repos;
            });
        } else
        {
            return cache.getUserRepos(user);
        }
    }
}
