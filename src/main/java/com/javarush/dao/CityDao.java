package com.javarush.dao;

import com.javarush.entities.City;

import java.util.List;
import java.util.Optional;

public interface CityDao {
    public List<City> getItems(int offset, int limit);

    public int getTotalCount();

    public Optional<City> getById(Integer id);
}
