package com.javarush;

import com.javarush.dao.CityDaoHibernate;

import com.javarush.dao.CreateSessionFactory;
import com.javarush.dao.FetchData;
import com.javarush.entities.City;
import com.javarush.entities.Country;
import com.javarush.entities.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.redis.Language;

import com.javarush.redis.RedisDb;

import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class DbApplication {

    public static void main(String[] args) {
        RedisDb redisDb= new RedisDb();
        SessionFactory sessionFactory = new CreateSessionFactory().getSessionFactory();
        FetchData fetchData= new FetchData();
        DbApplication dbApplication = new DbApplication();
        List<City> allCities = fetchData.fetchData(sessionFactory);
        List<CityCountry> preparedData = dbApplication.transformData(allCities);
        redisDb.pushToRedis(preparedData);

        sessionFactory.getCurrentSession().close();
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);
        long startRedis = System.currentTimeMillis();
        redisDb.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();
        long startMysql = System.currentTimeMillis();
        CityDaoHibernate cityDaoHibernate = new CityDaoHibernate(sessionFactory);
        cityDaoHibernate.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();
        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));
        shutdown(redisDb,sessionFactory);
    }

    private static void shutdown(RedisDb redisDb, SessionFactory sessionFactory) {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisDb.getRedisClient())) {
            redisDb.close();
      }
    }

    private List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
            CityCountry result = new CityCountry();
            result.setId(city.getId());
            result.setName(city.getName());
            result.setPopulation(city.getPopulation());
            result.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            result.setAlternativeCountryCode(country.getAlternativeCode());
            result.setContinent(country.getContinent());
            result.setCountryCode(country.getCode());
            result.setCountryName(country.getName());
            result.setCountryPopulation(country.getPopulation());
            result.setCountryRegion(country.getRegion());
            result.setCountrySurfaceArea(country.getSurfaceArea());

            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(countryLanguage -> {
                Language language = new Language();
                language.setLanguage(countryLanguage.getLanguage());
                language.setOfficial(countryLanguage.getOfficial());
                language.setPercentage(countryLanguage.getPercentage());
                return language;
            }).collect(Collectors.toSet());
            result.setLanguages(languages);
            return result;
        }).collect(Collectors.toList());
    }
}
