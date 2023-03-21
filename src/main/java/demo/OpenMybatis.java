package demo;

import demo.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OpenMybatis implements ApplicationRunner {
    public static SqlSession sqlSession;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        sqlSession = MybatisUtils.getSqlSession();
    }
}
