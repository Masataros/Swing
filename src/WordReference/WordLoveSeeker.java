package WordReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordLoveSeeker {

	public static int WordSeeker(File dir , String word) throws IOException {


			XWPFDocument docx = new XWPFDocument(new FileInputStream(dir));
			XWPFWordExtractor ex = new XWPFWordExtractor(docx); //XWPFWordExtractor を使うとドキュメントに含まれるテキストを簡単に取得できます。
			String tx = ex.getText();
			int judge;
			StringBuilder sb = new StringBuilder();
			//Stringクラスは本来固定の文字列となっており一度作成すると変更は出来ません。「+」演算子を使って文字列の連結を行う場合には、StringBuider

			if(tx.indexOf(word)!= -1){ //indexOfは文中の引数を探してあったら0以上、なかったら-1
				judge=1;
			}else{
				judge=0;
			}

			return judge;



		}
}
