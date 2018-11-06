//4J04岩崎悠紀
package pr2calc;

public class Matrix {
	// 必要なフィールド（インスタンス）変数を宣言せよ
	public double[][] m;
	public int numOfRow;
	public int numOfColumn;

	public Matrix(){
		// 行列の行数,列数を格納するインスタンス変数の値を0に初期化
		this.numOfRow = 0;
		this.numOfColumn = 0;
	}

	public Matrix(double[][] input){
		// 二次元配列 input の内容で、行列（インスタンス変数）を初期化せよ(例：配列 inputの0行0列目の値を、行列の0行0列目とする)
		m = new double[input.length][input[0].length];
		int i, j;
		for(i=0; i<input.length; i++){
			for(j=0; j<input[0].length; j++){
				m[i][j] = input[i][j];
			}
		}
	}

	public Matrix(double[] input){
		// 一次元配列 input の内容で、行列（インスタンス変数）を初期化せよ(例：行数は1、列数はinputの要素数とする)
		m = new double[1][input.length];
		int i;
		for(i=0; i<input.length; i++){
			m[0][i] = input[i];
		}
	}

	public int getNumOfRow(){
		return this.numOfRow;
	}

	public int getNumOfColumn(){
		return this.numOfColumn;
	}

	public double showsComponentOf(int rowIndex, int columnIndex){
		// 指定した範囲が存在しない場合
		if(this.m.length<rowIndex && this.m[0].length<columnIndex && rowIndex >0 && columnIndex > 0){
			System.out.println("指定する要素は存在しません.");
			System.exit(0);
		}
		// 指定された要素に対応する値を返す
		return this.m[rowIndex][columnIndex];
	}

	public void display(){
		// 行列内容の表示処理を実装せよ
		int i, j;
		for(i=0; i<m.length; i++){
			System.out.print("[ ");
			for(j=0; j<m[0].length; j++){
				System.out.printf("%.2f ", m[i][j]);
			}
			System.out.print("]＼n");
		}
	}

	// ベクトルAとBの内積 A・Bの結果を返す
	public double getInnerProduct(Matrix b){
		double sum = 0;
		int i;
		// Aが列ベクトルである場合、エラーメッセージを表示させて System.exit(0)
		if(this.m[0].length == 1){
			System.out.println("[ERROR] : Vector's length is not enough.");
			System.exit(0);
		}
		// A, B 双方とも行ベクトル、かつ、要素数が等しければ内積を計算
		if(this.m.length == 1 && b.m.length == 1 && this.m[0].length == b.m[0].length){
			for(i=0; i<b.m[0].length; i++){
				sum += this.m[0][i]*b.m[0][i];
			}
		}
		// Aが行ベクトル、Bが列ベクトルで、要素数が等しければ内積を計算
		else if(this.m.length == 1 && b.m[0].length == 1 && this.m[0].length == b.m.length){
			for(i=0; i<b.m[0].length; i++){
				sum += this.m[0][i]*b.m[i][0];
			}
		}
		// 内積計算が可能な条件を満たさない場合は、エラーメッセージを表示させてSystem.out.exit(0)
		else {
			System.out.println("[ERROR] : It does not meet the condition.");
			System.exit(0);
		}
		// 計算結果を返す
		return sum;
	}

	// 行列同士、もしくは行列とベクトルとの積を計算する
	public Matrix multiplyMatrix(Matrix target){
		Matrix buf = new Matrix(new double[this.m.length][target.m[0].length]);
		double sum;
		int i ,j ,k;
		// 掛けられる行列の列数と掛ける行列の行数が等しいなら
		if(this.multipliable(target)){
			// 積の計算処理を実装せよ
			for(i=0; i<this.m.length; i++){
				for(j=0; j<target.m[0].length; j++){
					sum = 0;
					for(k=0; k<this.m[0].length; k++){
						sum += this.m[i][k]*target.m[k][j];
					}
					buf.m[i][j] = sum;
				}
			}
		}
		else{
			System.out.println("要素数が計算できる組み合わせとなっていません");
			System.exit(0);
		}
		// 積の結果をMatrix型で返す
		return buf;
	}

	public boolean multipliable(Matrix y) {
		if(this.m[0].length == y.m.length ){
			return true;
		}else{
			System.out.println("要素数が計算できる組み合わせとなっていません");
			return false;
		}
	}

	public Matrix transpose(){
		double[][] buf = new double[this.m[0].length][this.m.length];
		int i, j;
		for(i=0; i<this.m.length; i++){
			for(j=0; j<this.m[0].length; j++){
				buf[j][i] = this.m[i][j];
			}
		}
		Matrix mat = new Matrix(buf);
		return mat;
	}
	
	public Matrix rotate(double rad){
		double theta = this.convertIntoRadian(rad);
		double[][] r = {{Math.cos(theta), -Math.sin(theta)},
			{Math.sin(theta), Math.cos(theta)}};
		Matrix mat = new Matrix(r);
		return mat.multiplyMatrix(this);
	}

	public static double convertIntoRadian(double theta){
		return Math.toRadians(theta);
	}

	public static void main(String[] args) {
		/*
		 * main メソッド中で今回作成した内積計算メソッドや行列同士、ベクトルと行列、
		 * 行列とベクトルの積を計算するメソッドが正常に動いているかを確認せよ。
		 */

		// 行列・ベクトル定義、および演算処理の一例 （あくまで一例です）　課題の要求を満たすよう、各自で加筆・修正してください
		double[][] mt0 = {{ -3.0},
			{ 3.0}},
			mt1 = {{ 2.0},
				{ -3.464}},
			mt2 = {{1, 2},
				{3, 4},
				{5, 6}};

		Matrix matrix0 = new Matrix(mt0);
		Matrix matrix1 = new Matrix(mt1);
		Matrix matrix2 = new Matrix(mt2);

		System.out.println("1.");
		matrix2.display();
		System.out.println("2.");
		matrix2.transpose().display();
		System.out.println("3.");
		matrix0.rotate(45).display();
		System.out.println("4.");
		matrix1.rotate(60).display();
//		Matrix mat1_0,mat1_1,mat2,mat3,mat4;

//		double[]
//			v0 = {2.0, -3.0, 7.0},
//			   v2 = {-1.0, -2.0, 2.0},
//			   v3 = {3.0, 7.0, -5.0, 2.0};
//		double[][] 
//			m0 = {
//				{1.0, 2.0,  3.0},
//				{3.0, 2.0, -1.0},
//				{4.0, 2.0,  6.0}},
//
//			   m1 = {
//				   { 8.0, 2.0},
//				   {-3.0, 2.0},
//				   { 1.0, 6.0}},
//
//			   v1 = {
//				   { 2.0},
//				   { -3.464}},
//			   
//			   m2 = {
//				   {2.0, -3.0},
//				   {4.0, 2.0}},
//
//			   m3 = {
//				   {-5.0, -3.0, 1},
//				   {-3.0, 3.0, 2.0}},
//
//			   m4 = {
//				   {5.0, 3.0, 1.0},
//				   {3.0, -3.0, 2.0}},
//
//			   d;
//
//		Matrix mat1_0 = new Matrix(v0);
//		Matrix mat1_1 = new Matrix(v2);
//		Matrix mat2_0 = new Matrix(m0);
//		Matrix mat2_1 = new Matrix(m4);
//		Matrix mat3_0 = new Matrix(v1);
//		Matrix mat3_1 = new Matrix(v3);
//		Matrix mat4_0 = new Matrix(m0);
//		Matrix mat4_1 = new Matrix(m1);
//		Matrix mat5_0 = new Matrix(m2);
//		Matrix mat5_1 = new Matrix(m3);
//		// 以下は、行列・ベクトル演算の実行＆結果表示の一例．不要であれば削除し，課題の条件を満たす記述を新たに追加すること
//		
//
//		System.out.println("m1_0 = ");	mat1_0.display();
//		System.out.println("m1_1 = ");	mat1_1.display();
//		System.out.println("m2_0 = ");	mat2_0.display();
//		System.out.println("m2_1 = ");	mat2_1.display();
//		System.out.println("m3_0 = ");	mat3_0.display();
//		System.out.println("m3_1 = ");	mat3_1.display();
//		System.out.println("m4_0 = ");	mat4_0.display();
//		System.out.println("m4_1 = ");	mat4_1.display();
//		System.out.println("m5_0 = ");	mat5_0.display();
//		System.out.println("m5_1 = ");	mat5_1.display();
////		System.out.println("v1 = ");	mat2.display();
//		
////		System.out.println(mat0.getInnerProduct(mat3));
//
//		System.out.println("＼n(1)");
//		System.out.println("m1_0 と m1_1 内積 ");
//		System.out.println(mat1_0.getInnerProduct(mat1_1));
//
//		System.out.println("＼n(2)");
//		System.out.println("m2_0 * m2_1 = ");
//		if(mat2_0.multipliable(mat2_1) == true)
//			(mat2_0.multiplyMatrix(mat2_1)).display();
//
//		System.out.println("＼n(3)");
//		System.out.println("m3_0 * m3_1 = ");
//		if(mat3_0.multipliable(mat3_1) == true)
//			(mat3_0.multiplyMatrix(mat3_1)).display();
//
//		System.out.println("＼n(4)");
//		System.out.println("m4_0 * m4_1 = ");
//		if(mat4_0.multipliable(mat4_1) == true)
//			(mat4_0.multiplyMatrix(mat4_1)).display();
//
//		System.out.println("＼n(5)");
//		System.out.println("m5_0 * m5_1 = ");
//		if(mat5_0.multipliable(mat5_1) == true)
//			(mat5_0.multiplyMatrix(mat5_1)).display();
//		System.out.println("");
//		System.out.println(mat1_0.convertIntoRadian(30));
	}
}
