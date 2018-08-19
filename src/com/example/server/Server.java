package com.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	
	// 服务端监听的端口地址
	private static final int port[] = {7878,7879,7880};
	
	public static void main(String [] args)  {
		
		for( int i = 0 ; i < port.length ; i++) {
			int temp = port[i];
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					Server helloServer = new Server();
					try {
						helloServer.server(temp);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}
	
	public void  server(int port) throws InterruptedException {
		
		/*
		 * EventLoopGroup 是在4.x版本中提出来的一个新概念。
		 * 用于channel的管理。服务端需要两个。
		 * 和3.x版本一样，一个是boss线程一个是worker线程。
		 * 
		 * 
		 * NioEventLoopGroup可以理解为一个线程池，内部维护了一组线程，
		 * 每个线程负责处理多个Channel上的事件，而一个Channel只对应于一个线程，
		 * 这样可以回避多线程下的数据同步问题。
		 */
		EventLoopGroup bossGrop = new NioEventLoopGroup();
		EventLoopGroup workerGrop = new NioEventLoopGroup();
		try {
			
			// ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求。
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGrop,workerGrop);
			b.channel(NioServerSocketChannel.class);
			
			//用于添加相关的Handler
			b.childHandler(new ServerInitializer());
			
			// 服务器绑定端口监听
		
			ChannelFuture f = b.bind(port).sync();
			
			// 监听服务器关闭监听
			f.channel().closeFuture().sync();
			
		} finally {
			// 优雅的全部关闭
			bossGrop.shutdownGracefully();
			workerGrop.shutdownGracefully();
		}
	}
	
}
