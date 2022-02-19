package entity;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	
	private String content;
	private Date time;
	private String sendIP;
	private String recvIP;
	/**
	 * @param content
	 * @param time
	 * @param sendIP
	 * @param recvIP
	 */
	public Message(String content, Date time, String sendIP, String recvIP) {
		this.content = content;
		this.time = time;
		this.sendIP = sendIP;
		this.recvIP = recvIP;
	}
	/**
	 * 
	 */
	public Message() {
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getSendIP() {
		return sendIP;
	}
	public void setSendIP(String sendIP) {
		this.sendIP = sendIP;
	}
	public String getRecvIP() {
		return recvIP;
	}
	public void setRecvIP(String recvIP) {
		this.recvIP = recvIP;
	}
	
	
}
