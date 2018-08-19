package com.example.server;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
    	// ChannelPipeline�����ڱ��洦�������Ҫ�õ���ChannelHandler��ChannelHandlerContext��
        ChannelPipeline pipeline = ch.pipeline();

        // ��("\n")Ϊ��β�ָ�� ������
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

        // �ַ������� �� ����
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        
        // ��Ϣ�ۺ���
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        
        // �Լ����߼�Handler
        pipeline.addLast("handler", new ServerHandler());
    }
}
