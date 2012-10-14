package fm.moe.android;

interface IMediaPlayerService {

	void attachAuxEffect(int effectId);
	int getAudioSessionId();
	int getCurrentPosition();
	int getDuration();
	boolean isLooping();
	boolean isPlaying();
	boolean isPrepared();
	boolean pause();
	boolean prepare();
	boolean prepareAsync();
	void release();
	void reset();
	boolean seekTo(int msec);
	boolean setAudioSessionId(int sessionId);
	void setAudioStreamType(int streamtype);
	void setAuxEffectSendLevel(float level);
	boolean setDataSource(String path);
	void setLooping(boolean looping);
	void setVolume(float leftVolume, float rightVolume);
	void setWakeMode(int mode);
	boolean start();
	boolean stop();

}