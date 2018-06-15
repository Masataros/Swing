package WordReference;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Swing_WR extends JFrame {
	private JTextField textField_Search;
	private JTextField textField_directory;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Swing_WR frame = new Swing_WR();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Swing_WR() {

		setBounds(240, 240, 295, 262);
		setTitle("WordReference");
		getContentPane().setLayout(null);

		JPanel Epanel = new JPanel();	//エラーダイアログに載せるオブジェクト
		JButton Ebutton = new JButton("OK");
		Epanel.add(Ebutton);

		JLabel label_Search = new JLabel("検索ワード");
		label_Search.setBounds(12, 30, 80, 13);
		getContentPane().add(label_Search);

		JLabel label_directory = new JLabel("ファイルパス");
		label_directory.setBounds(12, 75, 80, 13);
		getContentPane().add(label_directory);

		textField_Search = new JTextField();
		textField_Search.setBounds(99, 27, 156, 19);
		getContentPane().add(textField_Search);
		textField_Search.setColumns(10);

		textField_directory = new JTextField();
		textField_directory.setBounds(99, 72, 156, 19);
		getContentPane().add(textField_directory);
		textField_directory.setColumns(10);

		JButton Button_Search = new JButton("検索");
		Button_Search.setBounds(66, 163, 137, 36);
		getContentPane().add(Button_Search);

		JLabel lblcjavatest = new JLabel("入力例： C:\\User\\Documents");
		lblcjavatest.setForeground(Color.GRAY);
		lblcjavatest.setBounds(52, 112, 181, 13);
		getContentPane().add(lblcjavatest);


		Button_Search.addActionListener(new ActionListener() {	//検索ボタンのイベント
			public void actionPerformed(ActionEvent e) {

				String word = textField_Search.getText(); //検索ワード
				String dir = textField_directory.getText(); //ディレクトリ

				String path = dir;	//"C:\\javatest\\"
				File di = new File(path);
				String[] file_name = di.list(); //ディレクトリのファイル名を取得
				File[] file_dir = di.listFiles(); //ディレクトリを取得

				int Ace;
				int judge;
				int Y_Coordinate=10;


				while(word.equals("")){	//検索ワードが入力されていないときイベントのメソッドの外に戻る
					JOptionPane.showMessageDialog(Epanel, "検索ワードを入力してください", "Error",
							  JOptionPane.ERROR_MESSAGE);
					return;
				}


				try {

					Ace=file_dir.length;


					JFrame frame = new JFrame("検索 三(卍^o^)卍ﾄﾞｩﾙﾙ");	//検索結果の別窓
				    frame.setSize(280,550);
				    frame.setLocation(200,200);
				    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//closeオプション
				    frame.setVisible(true);
				    JLabel llabel=new JLabel("\""+textField_Search.getText()+"\""+" の検索結果です");
					llabel.setBounds(25, 7, 200,28 );
					llabel.setForeground(Color.red);
					llabel.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 12));
					frame.getContentPane().add(llabel);


					for(int i=0; i<file_dir.length; i++){

						try{
						judge=WordLoveSeeker.WordSeeker(file_dir[i],word); //検索ワードに該当したとき1がjudgeに入る

						if(judge==1){ //検索ワードがあるとき別窓にワード名が表示される

							JLabel label=new JLabel("・"+file_name[i]);
							label.setBounds(10, Y_Coordinate, 300, 100);
							frame.getContentPane().add(label);

							Y_Coordinate+=30;

						}

						} catch (IOException e1) {
							//JOptionPane.showMessageDialog(Epanel, "wordファイルが存在しません", "Error",
								//	  JOptionPane.ERROR_MESSAGE);

							continue;
						}

						repaint();	//画面を更新
					}

					for(int k=0;k<1;k++){	//調整用の空ループ
						JLabel label=new JLabel();
						label.setBounds(40, Y_Coordinate, 200, 100);
						frame.getContentPane().add(label);
					}

				} catch (NullPointerException e1) {	//例外処理でNullPointerExceptionがきたとき
					JOptionPane.showMessageDialog(Epanel, "ディレクトリの入力に不備があります", "Error",
							  JOptionPane.ERROR_MESSAGE);
				}



			}

		});


	}
}

