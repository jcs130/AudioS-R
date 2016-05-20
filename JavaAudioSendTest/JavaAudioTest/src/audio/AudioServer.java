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
import java.net.ServerSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * @author Zhongli Li
 *
 */
public class AudioServer {
	private audioServerThread audioS;
	private ServerSocket audio_sc;
	private int audio_Port = 6000;
	private sendAudioThread sat;
	private TargetDataLine line;

	public static void main(String[] args) {
		AudioServer a = new AudioServer();
		a.openRecDev();
		a.doSth();
	}

	/**
	 * @Author Zhongli Li Email: lzl19920403@gmail.com
	 */
	private void doSth() {
		try {
			audio_sc = new ServerSocket(this.audio_Port);
			System.out.println("音频服务器创建成功，端口：" + this.audio_Port);
			audioS = new audioServerThread(audio_sc);
			audioS.start();
			if (sat == null) {
				sat = new sendAudioThread(line, audioS);
			} else {
				sat.setAudioServer(audioS);

			}
			if (!sat.isAlive()) {
				System.out.println("Sending voice");
				sat.startTeaching();
				sat.startRecoding();
				sat.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openRecDev() {
		AudioFormat format = new AudioFormat(8000, 16, 1, true, false);// AudioFormat(float
		// sampleRate,
		// int
		// sampleSizeInBits,
		// int
		// channels,
		// boolean
		// signed,
		// boolean
		// bigEndian）
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format, line.getBufferSize());

		} catch (Exception ex) {
			return;
		}
		line.start();
	}

}
