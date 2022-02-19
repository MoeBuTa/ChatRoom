package util;

import java.util.HashMap;
import java.util.Map;

import mythread.Server;
import mythread.ServerClientSocket;

public class ServerManager {
	public static Server SERVER;
	public static Map<String, ServerClientSocket> CLIENTS = new HashMap<String, ServerClientSocket>();
	
}
