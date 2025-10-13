package com.example.lazyco.backend.core.DatabaseConf.DataInitilizer;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@DependsOn("dataSourceInitializer")
public class HibernateSequenceResetter implements InitializingBean {

  private final JdbcTemplate jdbcTemplate;

  public HibernateSequenceResetter(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    List<String> sequenceNames =
        jdbcTemplate.queryForList("SELECT sequence_name FROM hibernate_sequences", String.class);

    for (String table : sequenceNames) {
      Long maxId =
          jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM " + table, Long.class);
      jdbcTemplate.update(
          "UPDATE hibernate_sequences SET next_hi = ? WHERE sequence_name = ?", maxId, table);
    }
  }
}
