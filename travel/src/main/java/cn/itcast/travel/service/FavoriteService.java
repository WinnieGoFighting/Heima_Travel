package cn.itcast.travel.service;

public interface FavoriteService {
    public boolean isFavorite(int rid, int uid);
    void add(int rid, int uid);
}
