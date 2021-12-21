package org.springframework.lx.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.UUID;

/**
 * @author lx
 * @date 2021/12/13 21:52
 */
@Repository
public class StudentDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public  void insert(){
		String sql="INSERT INTO student VALUES(?,?,?)";
		String substring = UUID.randomUUID().toString().substring(0, 5);
		jdbcTemplate.update(sql,substring,"2","2");
	}
}
