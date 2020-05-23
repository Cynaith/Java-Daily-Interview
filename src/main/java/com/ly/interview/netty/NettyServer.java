package com.ly.interview.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @author: Cynaith
 **/
public class NettyServer {
    /**
     * 对应telnet操作
     * @param
     */
//    public void bind(int port) {
//        EventLoopGroup boss = new NioEventLoopGroup();
//        EventLoopGroup worker = new NioEventLoopGroup();
//        try {
//            ServerBootstrap server = new ServerBootstrap();
//            server.group(boss, worker).channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<Channel>() {
//
//                        @Override
//                        protected void initChannel(Channel ch) throws Exception {
//
//                            ch.pipeline().addLast(new MessageHandler());
//                        }
//
//                    });
//
//            ChannelFuture sync = server.bind(port).sync();
//            sync.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//
//            e.printStackTrace();
//        } finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
//
//    }

    public void bind(int port){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000, Unpooled.copiedBuffer("$".getBytes())));
                            channel.pipeline().addLast(new FixedLengthFrameDecoder(1));
                            channel.pipeline().addLast(new MyDecoder());
                            channel.pipeline().addLast(new StringHandler());

                        }
                    });
            ChannelFuture sync = server.bind(port).sync();
            sync.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new NettyServer().bind(8080);
    }
}
