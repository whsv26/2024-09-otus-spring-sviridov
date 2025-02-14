package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class KeyMappingRepositoryImpl implements KeyMappingRepository<String, Long> {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Long get(String sourceKey, Class<?> type) {
        var sql = "SELECT target_id FROM key_mapping WHERE source_id = :sourceId AND type = :type";
        var params = new MapSqlParameterSource(Map.of("sourceId", sourceKey, "type", type.getSimpleName()));
        return jdbc.queryForObject(sql, params, Long.class);
    }

    @Override
    public void set(String sourceKey, Long targetKey, Class<?> type) {
        var sql = "INSERT INTO key_mapping (source_id, target_id, type) VALUES (:sourceId, :targetId, :type)";
        var params = Map.of("sourceId", sourceKey, "targetId", targetKey, "type", type.getSimpleName());
        jdbc.update(sql, params);
    }
}
