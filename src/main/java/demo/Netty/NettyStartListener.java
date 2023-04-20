//package demo.Netty;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
////此类为NettyServer的自动开启类
////将代码交给Spring管理@Component
////实现ApplicationRunner接口，重写run方法，在启动后会自动执行。
//@Component
//public class NettyStartListener implements ApplicationRunner {
//
//
//    //自动装配的意思
//    @Autowired
//    private SocketServer socketServer;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        socketServer.start(8080);
//    }
//
//}
