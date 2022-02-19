package util;

import entity.Message;

public interface BackInterface {
	public void msgBack(Message msg);
	public void clientCome(String ip);
	public void clientLeave(String ip);
}
