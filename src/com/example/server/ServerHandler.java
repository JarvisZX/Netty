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
	
	// 获取客户端传来的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	
        // 收到消息直接打印输出
    	// localAddress()返回channel绑定的本地地址，remoteAddress()返回与Channel连接的远程地址
    	SocketAddress localAddress = ctx.channel().localAddress();
        System.out.println(localAddress.toString() + " Hello : " + msg);
        
        jedis.lpush(localAddress.toString(), localAddress.toString() + "_" + date.toString()+"_Msg:"+msg);

        
        // 返回客户端消息 - 我已经接收到了你的消息
        ctx.writeAndFlush("Received your message !\n");
    }
    
    /*
     * 
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * 
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
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
