package fm.moe.android;

interface IMediaPlayerService {

	void attachAuxEffect(int effectId);
	int getAudioSessionId();
	int getCurrentPosition();
	int getDuration();
	boolean isLooping();
	boolean isPlaying();
	boolean isPreparing();
	boolean isPrepared();
	boolean pause();
	void release();
	void reset();
	boolean seekTo(int msec);
	boolean setAudioSessionId(int sessionId);
	void setAudioStreamType(int streamtype);
	void setAuxEffectSendLevel(float level);
	void setLooping(boolean looping);
	void setVolume(float leftVolume, float rightVolume);
	void setWakeMode(int mode);
	boolean open(String path, boolean play);
	boolean start();
	boolean stop();

}