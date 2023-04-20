//package demo.Netty;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SocketServer {
//    public void start(int port) throws Exception {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch)
//                                throws Exception {
//                            // http 的解码器
//                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("http-codec",
//                                    new HttpServerCodec());
//                            //  负责将 Http 的一些信息例如版本
//                            // 和 Http 的内容继承一个 FullHttpRequesst
//                            pipeline.addLast("aggregator",
//                                    new HttpObjectAggregator(65536));
//                            // 大文件写入的类
//                            ch.pipeline().addLast("http-chunked",
//                                    new ChunkedWriteHandler());
//                            // websocket 处理类
//                            pipeline.addLast("handler",
//                                    new ServerHandler());
//                        }
//                    });
//            // 监听端口
//            Channel ch = b.bind(port).sync().channel();
//            ch.closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//
//}