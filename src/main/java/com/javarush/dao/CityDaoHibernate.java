package com.javarush.dao;

import com.javarush.entities.City;
import com.javarush.entities.CountryLanguage;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CityDaoHibernate implements CityDao {
    private final SessionFactory sessionFactory;

    public CityDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

@Override
    public List<City> getItems(int offset, int limit) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from  City c", City.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public int getTotalCount() {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery("select count(c)from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

@Override
    public Optional<City> getById(Integer id) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from  City c join " +
                "fetch c.country where c.id=:ID", City.class);
        query.setParameter("ID", id);
        Optional<City> cityOptional;
        try {
            cityOptional = Optional.of(query.getSingleResult());
        } catch (NoResultException exc) {
            cityOptional = Optional.empty();
        }
        return cityOptional;
    }

    public void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                Optional<City> city = getById(id);
                Set<CountryLanguage> languages = city.get().getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}
