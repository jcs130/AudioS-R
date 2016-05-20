package audio;

import java.io.*;
import java.net.Socket;

public class audioThread {
	private Socket s;
	private DataOutputStream captrueOutputStream;
	private audioServerThread ast;

	// 重载构造器
	public audioThread(Socket client, audioServerThread ast) throws Exception {
		this.s = client;
		this.ast = ast;
		try {
			captrueOutputStream = new DataOutputStream(s.getOutputStream());// 建立输出流
																			// 此处可以加套压缩流用来压缩数据
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	public void sendVoice(byte[] data, int numBytesRead) {
		try {
			// 向客户端发送Buffer数据
			captrueOutputStream.write(data, 0, numBytesRead);// 写入网络流
		} catch (Exception ex) {
			ast.removeMe(this);
			ex.printStackTrace();
		}
		try {
			captrueOutputStream.flush();
		} catch (IOException ex) {
			ast.removeMe(this);
			ex.printStackTrace();
		}
	}

}
