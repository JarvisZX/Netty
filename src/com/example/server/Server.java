package com.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	
	// ����˼����Ķ˿ڵ�ַ
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
		 * EventLoopGroup ����4.x�汾���������һ���¸��
		 * ����channel�Ĺ����������Ҫ������
		 * ��3.x�汾һ����һ����boss�߳�һ����worker�̡߳�
		 * 
		 * 
		 * NioEventLoopGroup�������Ϊһ���̳߳أ��ڲ�ά����һ���̣߳�
		 * ÿ���̸߳�������Channel�ϵ��¼�����һ��Channelֻ��Ӧ��һ���̣߳�
		 * �������Իرܶ��߳��µ�����ͬ�����⡣
		 */
		EventLoopGroup bossGrop = new NioEventLoopGroup();
		EventLoopGroup workerGrop = new NioEventLoopGroup();
		try {
			
			// ServerBootstrap�����ʼ��netty�����������ҿ�ʼ�����˿ڵ�socket����
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGrop,workerGrop);
			b.channel(NioServerSocketChannel.class);
			
			//���������ص�Handler
			b.childHandler(new ServerInitializer());
			
			// �������󶨶˿ڼ���
		
			ChannelFuture f = b.bind(port).sync();
			
			// �����������رռ���
			f.channel().closeFuture().sync();
			
		} finally {
			// ���ŵ�ȫ���ر�
			bossGrop.shutdownGracefully();
			workerGrop.shutdownGracefully();
		}
	}
	
}
