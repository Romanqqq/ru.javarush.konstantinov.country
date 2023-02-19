package com.javarush.dao;

import com.javarush.entities.Country;

import java.util.List;

public interface CountryDao {

    public List<Country> getAll();
}
