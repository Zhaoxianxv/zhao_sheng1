/**
 * 
 */
package com.gy.aac.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.gy.aac.AbstractRecord;
import com.sinaapp.bashell.VoAACEncoder;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class AacRecorder extends AbstractRecord {

	private final static String TAG = AacRecorder.class.getSimpleName();

	// ===========================================================
	// ״̬
	// ===========================================================

	protected State state = State.NO_ACTION;

	// ===========================================================

	// ===========================================================
	private final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
	private final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

	private final static int BITRATE = 32000;
	private final static short CHANNELS = 1;
	private final static short ADTS_USED = 1;
	// ===========================================================

	// ===========================================================

	private int mSampleRate;
	private int mBufferSize;
	private int minSize;

	private VoAACEncoder aacEncoder;
	private AudioRecord mRudioRecorder;
	private String savePath;
	private FileOutputStream fos;

	private Context context;
	private long mStartTime = 0;
	private long recordTime = 0;
	private String date;

	private static Handler handler = new Handler();

	public AacRecorder(Context context, int sampleRate, int bufferSizeInBytes) {
		this.context = context;
		this.mSampleRate = sampleRate;
		this.mBufferSize = bufferSizeInBytes;
		init();
	}

	public AacRecorder(Context context, int sampleRate, int bufferSizeInBytes,
			String savePath) {
		this.context = context;
		this.mSampleRate = sampleRate;
		this.mBufferSize = bufferSizeInBytes;
		this.savePath = savePath;
		init();
	}

	private void init() {
		aacEncoder = new VoAACEncoder();
		minSize = AudioRecord.getMinBufferSize(mSampleRate, CHANNEL_CONFIG,
				AUDIO_FORMAT);
		minSize = Math.max(mBufferSize, minSize);
	}

	public String getSavePath() {
		return savePath;
	}

	public String getDate() {
		return date;
	}

	@Override
	public void start() {
		mStartTime = 0;
		recordTime = 0;
		savePath = Environment.getExternalStorageDirectory() + "/Record/"
				+ getData() + ".aac";
		date = getRecordDate();
		createFile(savePath);

		state = State.RUN;
		mRudioRecorder = new AudioRecord(AUDIO_SOURCE, mSampleRate,
				CHANNEL_CONFIG, AUDIO_FORMAT, minSize);

		recordRunnable();
	}

	@Override
	public void stop() {
		if (state == State.PAUSE) {
			encodeAac();
			state = State.END;
			if (listener != null) {
				listener.onEnd(recordTime, state);
			}
			mStartTime = 0;
			recordTime = 0;
		}

		state = State.END;
	}

	@Override
	public void pause() {
		state = State.PAUSE;
	}

	@Override
	public void resume() {
		state = State.RUN;
		mRudioRecorder = new AudioRecord(AUDIO_SOURCE, mSampleRate,
				CHANNEL_CONFIG, AUDIO_FORMAT, minSize);
		recordRunnable();
	}

	@Override
	public void reset() {
		mStartTime = 0;
		recordTime = 0;
		state = State.NO_ACTION;
		handler.post(new Runnable() {
			@Override
			public void run() {
				listener.onRecordProgress(0);
			}
		});
		deleteFile(savePath);
	}

	private void recordRunnable() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					fos = new FileOutputStream(savePath, true);
					RandomAccessFile accessFile = new RandomAccessFile(
							new File(savePath), "rw");
					byte[] tempBuffer = new byte[mBufferSize];

					mRudioRecorder.startRecording();
					mStartTime = System.currentTimeMillis();
					while (state == State.RUN) {
						int bufferRead = mRudioRecorder.read(tempBuffer, 0,
								mBufferSize);
						if (bufferRead > 0) {
							try {
								accessFile.seek(accessFile.length());
								accessFile.write(tempBuffer, 0,
										tempBuffer.length);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						long newTime = System.currentTimeMillis();
						recordTime = recordTime + newTime - mStartTime;
						mStartTime = newTime;
						if (listener != null) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									listener.onRecordProgress(recordTime);
								}
							});
						}
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (state == State.END) {
								encodeAac();
							}
							if (listener != null) {
								listener.onEnd(recordTime, state);
							}
						}
					});
					accessFile.close();
					mRudioRecorder.stop();
					mRudioRecorder.release();
					mRudioRecorder = null;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void encodeAac() {
		String savePath2 = Environment.getExternalStorageDirectory()
				+ "/Record/" + getData() + ".aac";
		createFile(savePath2);

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(savePath);
			fos = new FileOutputStream(savePath2);
			aacEncoder.Init(mSampleRate, BITRATE, CHANNELS, ADTS_USED);

			byte[] buffer = new byte[1024];
			int length = fis.read(buffer);
			byte[] ret = aacEncoder.Enc(buffer);

			while (length != -1) {
				fos.write(ret, 0, ret.length);
				length = fis.read(buffer);
				ret = aacEncoder.Enc(buffer);
			}
			aacEncoder.Uninit();

			rename(savePath2, savePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class MusicSannerClient implements MediaScannerConnectionClient {

		private String path;
		private Context context;
		private MediaScannerConnection scanner;

		public MusicSannerClient(String path, Context context) {
			this.path = path;
			this.context = context;
			scanner = new MediaScannerConnection(context, this);
		}

		public void startScan() {
			scanner.connect();
		}

		@Override
		public void onMediaScannerConnected() {
			scanner.scanFile(path, null);
		}

		@Override
		public void onScanCompleted(String path, Uri uri) {
			scanner.disconnect();

			handler.post(new Runnable() {

				@Override
				public void run() {
					if (listener != null) {
						listener.onEnd(recordTime, state);
					}
				}
			});
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String getData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}

	public String getRecordDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public void rename(String name) {
		String path1 = Environment.getExternalStorageDirectory() + "/Record/"
				+ name + ".aac";
		rename(savePath, path1);
		savePath = path1;
	}

	private void rename(String oldPath, String newPath) {
		File file = new File(oldPath);
		file.renameTo(new File(newPath));
	}

	private void createFile(String path) {
		File file = new File(path);
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteFile(String path) {
		if (path != null) {
			File file = new File(savePath);
			file.deleteOnExit();
		}
	}

}
