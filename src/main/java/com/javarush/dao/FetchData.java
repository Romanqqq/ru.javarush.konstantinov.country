package com.javarush.dao;

import com.javarush.entities.City;
import com.javarush.entities.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class FetchData {

    public List<City> fetchData(SessionFactory sessionFactory) {
        CityDaoHibernate cityDaoHibernate= new CityDaoHibernate(sessionFactory);
        CountryDaoHibernate countryDaoHibernate= new CountryDaoHibernate(sessionFactory);

        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            List<Country> countries = countryDaoHibernate.getAll();
            int totalCount = cityDaoHibernate.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDaoHibernate.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }
}
