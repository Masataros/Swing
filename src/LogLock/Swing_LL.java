package LogLock;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Swing_LL extends Thread implements Runnable {

	private JFrame frmlog;
	private JTextField text_number;
	private JTextField text_name;
	private JTextField text_tel;
	private JTextField text_obj;
	private JLabel label_dotime;
	private JButton button_start;
	private Process process;
	private Runtime rt;

	private static JPanel Epanel;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		FileOutputStream fos = null;//----------多重起動を防ぐための処理--------------
		try {
			fos = new FileOutputStream("lockfile");		//ロックファイル(起動判定)を作る
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		FileChannel fchan = fos.getChannel();
		FileLock flock = null;

		try {
			flock = fchan.tryLock(); //ロックに成功すると新規獲得ロックを表すロックオブジェクト返ってくるが失敗するとnull
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (null == flock) {
			// 例外 throw 等、ロックが取得できなかったときの処理を記述
			JOptionPane.showMessageDialog(Epanel, "現在使用中です", "Error",
					  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}//----------------------------------------------------------------------------

		//アプリケーションを起動
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Swing_LL window = new Swing_LL();
					window.frmlog.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Swing_LL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		Calendar cal = Calendar.getInstance();	//開始時刻を取得

		int yer = cal.get( Calendar.YEAR);
		int mon = cal.get( Calendar.MONTH);
		int day = cal.get( Calendar.DATE);
		int hor = cal.get( Calendar.HOUR_OF_DAY );
		int min = cal.get( Calendar.MINUTE );

		String time = ( yer + " , " + (mon+1) + "/" + day + " , "+ hor +  ":" + min);
		String day_now = ( yer + " , " + (mon+1) + "/" + day );
		String time_now =( hor +  ":" + min );


		Epanel = new JPanel();	//エラーダイアログに載せるオブジェクト
		JButton Ebutton = new JButton("OK");
		Epanel.add(Ebutton);

		frmlog = new JFrame();
		frmlog.setTitle("利用管理");
		frmlog.setBounds(100, 100, 298, 446);
		frmlog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmlog.getContentPane().setLayout(null);

		button_start = new JButton("開始");
		button_start.setBounds(68, 287, 140, 36);
		frmlog.getContentPane().add(button_start);

		text_number = new JTextField();
		text_number.setBounds(120, 24, 109, 19);
		frmlog.getContentPane().add(text_number);
		text_number.setColumns(10);

		text_name = new JTextField();
		text_name.setColumns(10);
		text_name.setBounds(120, 65, 109, 19);
		frmlog.getContentPane().add(text_name);

		JLabel label_number = new JLabel("学籍番号");
		label_number.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		label_number.setBounds(42, 27, 50, 13);
		frmlog.getContentPane().add(label_number);

		JLabel label_name = new JLabel("氏名");
		label_name.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		label_name.setBounds(42, 68, 50, 13);
		frmlog.getContentPane().add(label_name);

		JLabel label_time = new JLabel(time);
		label_time.setBounds(82, 212, 140, 13);
		frmlog.getContentPane().add(label_time);

		JLabel label_hoge1 = new JLabel("経過時間");
		label_hoge1.setBounds(58, 248, 61, 13);
		frmlog.getContentPane().add(label_hoge1);

		label_dotime = new JLabel("計測中");
		label_dotime.setBounds(145, 248, 50, 13);
		frmlog.getContentPane().add(label_dotime);

		JLabel label_test = new JLabel();
		label_test.setBounds(166, 248, 50, 13);
		frmlog.getContentPane().add(label_test);

		JLabel label_tel = new JLabel("電話番号");
		label_tel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		label_tel.setBounds(42, 111, 50, 13);
		frmlog.getContentPane().add(label_tel);

		text_tel = new JTextField();
		text_tel.setColumns(10);
		text_tel.setBounds(120, 108, 109, 19);
		frmlog.getContentPane().add(text_tel);

		JLabel label_obj = new JLabel("作るもの");
		label_obj.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		label_obj.setBounds(42, 173, 50, 13);
		frmlog.getContentPane().add(label_obj);

		text_obj = new JTextField();
		text_obj.setColumns(10);
		text_obj.setBounds(120, 170, 109, 19);
		frmlog.getContentPane().add(text_obj);

		JLabel label_cation = new JLabel("※間のハイフン(-)はいりません");
		label_cation.setForeground(Color.RED);
		label_cation.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		label_cation.setBounds(68, 132, 181, 13);
		frmlog.getContentPane().add(label_cation);


		button_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//-------------------------------開始ボタンが押されたとき----------------------------------
				String log=null;
				String log1=null;
				String[] env = new String[1];
				env[0] = "lang=ja"; //3Dソフトの日本語化パッチ
				File dir = new File("C:\\Mutoh\\pronterface\\PronterfaceMF-2200D"); //ディレクトリ

				try{
					//学番と名前がある程度ちゃんと入力できているか
					if(text_number.getText().length() >= 4 && text_name.getText().length() >= 1
							&& text_tel.getText().length() >= 9 && text_obj.getText().length() >= 1)
					{
				      //年月日,名前,学番,開始時間をファイルに書き込む
					  FileWriter fw =new FileWriter("log.txt",true);
					  log = (day_now + " , " + text_number.getText() + " , " + text_name.getText() + " , " +text_tel.getText() + " , "+ time_now);
					  fw.write(log);

					  start();//別スレッド(ストップウォッチ)の開始

					  button_start.setEnabled(false);


					  //ファイル(プロセス)を起動
					  try {
					      rt = Runtime.getRuntime();
					      process = rt.exec("C:\\Program Files (x86)\\SRP Player\\SRPPlayer.exe");
					    } catch (IOException ex) {
					      ex.printStackTrace();
					    }

					  process.waitFor();	//プロセスが終了するまで待機
					  stop();				//別スレッドの(ストップウォッチ)の終了

					  button_start.setText("終了");
					  button_start.setEnabled(false);	//それぞれのインスタンスをホワイトアウト
					  text_number.setEnabled(false);
					  text_name.setEnabled(false);
					  text_tel.setEnabled(false);
					  text_obj.setEnabled(false);

					  Calendar cal1 = Calendar.getInstance();	//終了時間を取得
					  int hor1 = cal1.get( Calendar.HOUR_OF_DAY );
					  int min1 = cal1.get( Calendar.MINUTE );
					  log1 = (" , "+" ～ " + " , " +hor1 + ":" + min1 + " , " + text_obj.getText() );
					  fw.write(log1+"\r\n");	//終了時間を書き込む
					  fw.close();	//ファイルを閉じる

					  sleep(5000);
					  System.exit(0);


					}
					else{
						//入力不備時のエラーダイアログ
						JOptionPane.showMessageDialog(Epanel, "入力に不備があります", "Error",
								  JOptionPane.ERROR_MESSAGE);
					}

				}catch (IOException ex) {
		            ex.printStackTrace();
		        } catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});//--------------------------------------------------------------------------------------------------------------------------------------------


	}


	public void run(){//-----------別スレッドの処理(ストップウォッチ)----------
		int i=0;
		while(true){
			label_dotime.setText(Integer.toString(i)+"分");
			try{
				sleep(60000);//1分処理のディレイ
				i++;
			}catch(InterruptedException e){
				break;
			}
		}
	}//--------------------------------------------------------------------------
}




//System.out.println("0"); //テスト用


