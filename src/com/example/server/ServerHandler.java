package com.example.server;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import redis.clients.jedis.Jedis;


public class ServerHandler extends SimpleChannelInboundHandler<String> {
	Jedis jedis = new Jedis("localhost");
	Date date = new Date();
	
	// ��ȡ�ͻ��˴�������Ϣ
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	
        // �յ���Ϣֱ�Ӵ�ӡ���
    	// localAddress()����channel�󶨵ı��ص�ַ��remoteAddress()������Channel���ӵ�Զ�̵�ַ
    	SocketAddress localAddress = ctx.channel().localAddress();
        System.out.println(localAddress.toString() + " Hello : " + msg);
        
        jedis.lpush(localAddress.toString(), localAddress.toString() + "_" + date.toString()+"_Msg:"+msg);

        
        // ���ؿͻ�����Ϣ - ���Ѿ����յ��������Ϣ
        ctx.writeAndFlush("Received your message !\n");
    }
    
    /*
     * 
     * ���� channelActive ���� ��channel�����õ�ʱ�򴥷� (�ڽ������ӵ�ʱ��)
     * 
     * channelActive �� channelInActive �ں���������н����������Ȳ�����ϸ������
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        
        ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
        
        super.channelActive(ctx);
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	// TODO Auto-generated method stub
    	super.exceptionCaught(ctx, cause);
    }
    

}
