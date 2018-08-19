package com.example.redis;

import java.util.List;

import redis.clients.jedis.Jedis;

public class RedisServer {
	
	private static final int port[] = {7878,7879,7890};
	public static String host = "127.0.0.1";
	private static Jedis jedis;
	public static void main(String[] args) {
		jedis = new Jedis("localhost");
		System.out.println("Redis ÒÑÁ¬½Ó");
		
		for(int i = 0 ; i < port.length ; i++) {
			String address = "/" + host + ":" + port[i];
			List<String> list = jedis.lrange(address,0,10);
			for (String string : list) {
				System.out.println(string);
			}
			System.out.println("--------------------------------------");
		}
	}

}
