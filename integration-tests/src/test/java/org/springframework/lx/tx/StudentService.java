package org.springframework.lx.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lx
 * @date 2021/12/13 21:51
 */
@Service
public class StudentService {
	@Autowired
	private StudentDao studentDao;

	@Transactional(rollbackFor=Exception.class)
	public  void insert(){
		studentDao.insert();
		System.out.println("插入完成");
		int i=10/0;
	}

}
