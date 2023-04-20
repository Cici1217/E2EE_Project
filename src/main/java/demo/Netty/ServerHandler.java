//package demo.Netty;
//
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import io.netty.handler.codec.http.websocketx.WebSocketFrame;
//import io.netty.util.concurrent.GlobalEventExecutor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
//    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame data) throws Exception {
//        String content = String.valueOf(data.content());
//        System.out.println("接收到的数据：" + content);
//        String[] strings = content.split(" ");
//        for (Channel channel : clients) {
//            channel.writeAndFlush(new TextWebSocketFrame(strings[1] + ":" + strings[0]));
//        }
//    }
//
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        // 当客户端连接服务端之后，获取客户端的channel，并且放到ChannelGroup中去进行管理
//        clients.add(ctx.channel());
//
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        // 这步是多余的，当断开连接时候ChannelGroup会自动移除对应的channel
//        clients.remove(ctx.channel());
//        System.out.println(ctx.channel().id().asLongText());
//    }
//}