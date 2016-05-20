/** 
 * Copyright (C) 2016 City Digital Pulse - All Rights Reserved
 *  
 * Author: Zhongli Li
 *  
 * Design: Zhongli Li and Shiai Zhu
 *  
 * Concept and supervision Abdulmotaleb El Saddik
 *
*/
package audio;

import java.io.IOException;
import java.net.Socket;


/**
 * @author Zhongli Li
 *
 */
public class AudioClient {
	public static void main(String[] args) {
		AudioClient a=new AudioClient();
		a.doSth();
	}

	/**
	 * @Author  Zhongli Li Email: lzl19920403@gmail.com
	 */
	private void doSth() {
		Socket cli;
		try {
			cli = new Socket("localhost", 6000);
			Playback player = new Playback(cli);
			player.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
