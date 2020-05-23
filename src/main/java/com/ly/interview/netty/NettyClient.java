package com.ly.interview.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: Cynaith
 **/
public class NettyClient {
    public void bind(String ip,int port){
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(worker).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new StringEncoder());
                }
            });
            ChannelFuture connect = b.connect(ip, port);
            // connect是一个异步操作，要保证连接创建成功才可以发消息。
            connect.sync();
            connect.channel().writeAndFlush("hello");
            connect.channel().close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyClient().bind("localhost",8080);
    }
}
