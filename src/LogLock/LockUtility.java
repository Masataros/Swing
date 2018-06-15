package LogLock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public final class LockUtility {

	private LockUtility () {
		// インスタンスの生成は許可しない。
	}

	/**
	 * ロックの取得を試みます。<br/>
	 * ロック取得に失敗した場合は、AlreadyBootedException を throw します。
	 *
	 * @return
	 */
	public static synchronized boolean lock() throws IOException {

		try {

			fos = new FileOutputStream("lockfile");
			fchan = fos.getChannel();
			flock = fchan.tryLock();
		}
		catch (IOException e) {

			throw e;
		}

		if (null == flock) {

			throw new AlreadyBootedException();
		}

		// ロック解放処理をシャットダウンフックに登録
		Runtime.getRuntime().addShutdownHook(new Thread(new LockShutdownHook()));

		return true;
	}

	/**
	 * ロックを解放します。
	 */
	public static void release() {

		try {

			flock.release();
			fchan.close();
			fos.close();

			new File("lockfile").delete();
		}
		catch (IOException e) {

			e.printStackTrace();
		}

		return;
	}

	//*** クラス変数
	private static FileOutputStream fos;
	private static FileChannel fchan;
	private static FileLock flock;
}


class LockShutdownHook implements Runnable {


	public void run() {

		LockUtility.release();

		return;
	}
}


class AlreadyBootedException extends RuntimeException {

	AlreadyBootedException() {

		super("プログラムの多重起動は出来ません。");

		return;
	}
}