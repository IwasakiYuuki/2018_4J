package pr2calc;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SourceExample{  

	public int a;            // 左の3変数・・・フィールド変数（メソッドの外にある）。
	public int[][] b;        // クラス内の、main()メソッドを除く全メソッドで利用可能
	public String str;       

	/* 以下のメソッド（関数）はコンストラクタ。オブジェクトの生成（& 初期化）を行う 
	 * 引数の設定は自由。引数が異なれば、複数用意しても良い
	 */
	public SourceExample(){   // 各変数の初期化を実行（例なので、中身は適当です）
		int h,i;    // メソッド内で宣言される変数は、メソッド内のみで利用可

		this.a = -1;    // フィールド変数として定義された変数は、クラス内どこでも利用可（this.a ・・・ "この(= this)オブジェクト(インスタンス)自身の持つ変数a"）
		this.b = new int[2][2];

		/* java では、配列と文字列はオブジェクトとして用意されているため、新しい配列、
		 * 文字列を宣言する時には "new" を使う(上の例では、b を2×2の整数型二次元配列
		 * として宣言している）
		 */

		for(h=0;h<this.b.length;h++){		// 二次元配列の値を -1 で初期化
			for(i=0;i<this.b[0].length;i++)		// 配列に関しては、"new" を用いずに
				this.b[h][i] = -1;			// 初期化することも可能（詳細はmain
		}						// メソッド内を参照）

		// this.b.length : 配列の第一要素の要素数、this.b[0].length : 配列の第二要素の要素数

		this.str = "";	// 文字列型はオブジェクトとして用意されると記述したが、非常によく
	}			// 用いられる型であるため、左のような記法も認められている
	// （ new String(""); という書き方でも当然OK ）


	// フィールド変数a, b, strの値を内部で初期化（設定）するコンストラクタをもう一つ記述する
	public SourceExample(int a, int[][] b, String str){
		this.setA(a);
		this.setB(b);
		this.setStr(str);
	}


	/* 以降、mainメソッド以外のメソッドは、コンストラクタで生成されたオブジェクト
	 * を介して実行される
	 */

	public void setA(int value){    // メソッドを通して、フィールド変数の値を設定する
		this.a = value;		// （setterと呼ばれる）
	}

	public int getA(){		// メソッドを通して、フィールド変数の値を返す
		return this.a;		// （getterと呼ばれる）
	}

	public void setB(int[][] value){
		//    this.b = new int[value.length][value[0].length];
		this.b = value;
	}

	public int[][] getB(){
		return this.b;
	}

	public void setStr(String value){
		this.str = value;
	}

	public String getStr(){
		return this.str;
	}

	public void showAllContentsOfB(){
		// 各自で必要な内容を記述すること
		int i=0, h=0;
		for(h=0;h<b.length;h++){
			for(i=0;i<b[0].length;i++)
				if(((i+1) != 3)){
					System.out.print(b[h][i]+", ");
				}else{
					System.out.print(b[h][i]);
				}
			System.out.println("");
		}
	}

	public SourceExample(String fileName){
		try{
			// ファイル"fileName"から、データを読み込むメソッドを呼び出す
			loadData(fileName);
		}catch(IOException e){  System.out.println("ファイルからの入力に失敗しました。");}
	}


	public static void main(String[] args) {
		SourceExample    ex;    // SourceExample クラスのオブジェクト ex を宣言
		int value[][] = {{1,2,3},{4,5,6},{7,8,9}}; // "new"を用いない、二次元配列を初期化する書き方

		//ex = new SourceExample();    // SourceExample クラスのオブジェクト ex を生成
		//ex.setA(10); // ex オブジェクトのフィールド this.a の値を10に変更；
		// 上の二行をコメントアウトした上で、aを3、bを配列 value、str を文字列 "Hello World." として
		// 初期化するコンストラクタを用いて ex を生成せよ
		ex = new SourceExample(3, value, "Hello World.");

		System.out.println("オブジェクトのaフィールドの値は"+ex.getA()+"です");
		ex.showAllContentsOfB();
		System.out.println(ex.getStr());

	}
}
