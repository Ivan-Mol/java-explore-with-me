package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.Statistic;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("select new ru.practicum.ewm.model.EndpointHit(s.app, s.uri, count(s.ip))" +
            "from Statistic s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<EndpointHit> getStatistic(LocalDateTime timestampStart, LocalDateTime timestampEnd, Collection<String> uris);

    @Query("select new ru.practicum.ewm.model.EndpointHit(s.app, s.uri, count(distinct s.ip))" +
            "from Statistic s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<EndpointHit> getStatisticDistinct(LocalDateTime timestampStart, LocalDateTime timestampEnd, Collection<String> uris);

    @Query("select new ru.practicum.ewm.model.EndpointHit(s.app, s.uri, count(distinct s.ip))" +
            "from Statistic s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<EndpointHit> getAllStatisticDist(LocalDateTime timestampStart, LocalDateTime timestampEnd);

    @Query("select new ru.practicum.ewm.model.EndpointHit(s.app, s.uri, count(s.ip))" +
            "from Statistic s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<EndpointHit> getAllStatistic(LocalDateTime timestampStart, LocalDateTime timestampEnd);

}