package audio;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class audioServerThread extends Thread{
	private ServerSocket audioServer;
	private static ArrayList<audioThread> audioClientList;

	public audioServerThread(ServerSocket audioServer) {
		this.audioServer = audioServer;
		this.audioClientList = new ArrayList<audioThread>();
	}

	public void run() {
		try {
			while (true) {
				System.out.println("等待客户端连接⋯⋯");
				Socket client = audioServer.accept();// 等待用户进入
				System.out.println("客户及连接："
						+ client.getRemoteSocketAddress().toString());
				// 启动一个线程，去处理这个连接对象
				audioThread at=new audioThread(client,this);
				audioClientList.add(at);
//				System.out.println("add"+audioClientList.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			audioClientList.clear();
//			System.out.println("del"+audioClientList.size());
		}

	}
	
	public void removeMe(audioThread ac){
		audioClientList.remove(ac);
	}

	public ArrayList<audioThread> getAudioClients() {
		return audioClientList;
	}

}
