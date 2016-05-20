package audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public class sendAudioThread extends Thread {
	private TargetDataLine line;
	private audioServerThread audioS;
	private boolean isTeaching = false;
	private boolean isRecoding = false;
	// 定义字节数组输入输出流
	private ByteArrayOutputStream baos = null;
	private ByteArrayInputStream bais = null;
	// 定义音频输入流
	private AudioInputStream ais = null;
	private String startTime = "";
	// 定义录音格式
	private AudioFormat af = null;

	byte[] data = new byte[1024];// 此处的1024可以情况进行调整，应跟下面的1024应保持一致

	public sendAudioThread(TargetDataLine line, audioServerThread audioS) {
		this.line = line;
		this.audioS = audioS;

	}

	public void run() {
		int numBytesRead = 0;
		 System.out.println("yuyin");
		while (true) {
			numBytesRead = line.read(data, 0, 1024);// 取数据（1024）的大小直接关系到传输的速度，一般越小越快，
			if (isRecoding || isTeaching) {
				if (isRecoding) {
					if (numBytesRead > 0) {
						baos.write(data, 0, numBytesRead);
					}
				}
				if (isTeaching) {
					// System.out.println("isteach");
//					System.out.println("client:"+audioS.getAudioClients().size());
					try {

						for (int i = 0; i < audioS.getAudioClients().size(); i++) {
							audioS.getAudioClients().get(i)
									.sendVoice(data, numBytesRead);
						}
//						 System.out.println("sendVoice"+numBytesRead);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void startTeaching() {
		this.isTeaching = true;

	}

	public void stopTeaching() {
		// System.out.println("stopTeaching");
		this.isTeaching = false;
	}

	public void startRecoding() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		// 记录开录音的时间
		startTime = df.format(new Date());
		baos = new ByteArrayOutputStream();
		this.isRecoding = true;

	}

	public void stopRecoding() {
		this.isRecoding = false;
		af = getAudioFormat();

		byte audioData[] = baos.toByteArray();
		bais = new ByteArrayInputStream(audioData);
		ais = new AudioInputStream(bais, af, audioData.length
				/ af.getFrameSize());
		// 定义最终保存的文件名
		File file = null;
		// 写入文件
		try {
			// 以当前的时间命名录音的名字
			// 将录音的文件存放到F盘下语音文件夹下
			File filePath = new File("D:\\eLeanClass");
			if (!filePath.exists()) {// 如果文件不存在，则创建该目录
				filePath.mkdir();
			}
			file = new File(filePath.getPath() + "\\" + startTime + ".mp3");
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {

				if (bais != null) {
					bais.close();
				}
				if (ais != null) {
					ais.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 设置AudioFormat的参数
	public AudioFormat getAudioFormat() {
		// 下面注释部分是另外一种音频格式，两者都可以
		// AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		// float rate = 8000f;
		// int sampleSize = 16;
		// String signedString = "signed";
		// boolean bigEndian = true;
		// int channels = 1;
		// return new AudioFormat(encoding, rate, sampleSize, channels,
		// (sampleSize / 8) * channels, rate, bigEndian);
		// 采样率是每秒播放和录制的样本数
		float sampleRate = 8000;
		// 采样率8000,11025,16000,22050,44100
		// sampleSizeInBits表示每个具有此格式的声音样本中的位数
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 单声道为1，立体声为2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	public void setAudioServer(audioServerThread audioS) {
		this.audioS=audioS;
	}
}
