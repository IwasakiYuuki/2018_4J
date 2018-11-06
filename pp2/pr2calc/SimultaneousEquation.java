//4J04岩崎悠紀　課題番号５
package pr2calc;

public class SimultaneousEquation extends Matrix {
	public double[] answers;

	public SimultaneousEquation(double[][] input){
		super(input);
		answers = new double[this.m.length];
	}

	public void normalize(int n){
		int i;
		double buf = this.m[n][n];
		for(i=0; i<this.m[n].length; i++){
			this.m[n][i] /= buf;
		}
	}

	public void subtractRowFrom(int n, int org) {
		int i;
		double k = this.m[org][n];
		for(i=0; i<this.m[org].length; i++){
			this.m[org][i] -= (this.m[n][i] * k);
		}
	}

	public void solveByGaussJordan(){
		int i, j;
		this.display();
		System.out.println("");
		for(i=0; i<this.m.length; i++){
			this.normalize(i);
			for(j=0; j<this.m.length; j++){
				if(j==i)continue;
				this.subtractRowFrom(i, j);
			}
			System.out.println((i+1)+"行"+(i+1)+"列目が１となるように割り，他の行の"+(i+1)+"列目が０となるように引く");
			this.display();
			System.out.println("");
		}
		System.out.println("Answer:");
		for(i=0; i<this.m.length; i++){
			this.answers[i] = this.m[i][this.m.length];
			System.out.printf("x%d = %.2f", i+1, this.answers[i]);
			if(i!=(this.m.length-1)){
				System.out.print(", ");
			}else{
				System.out.print(".\n");
			}
		}
	}

	public static void main(String[] args) {
		double[][] test1 = {{2.0, 1.0, 3.0, 4.0, 2.0},
				{3.0, 2.0, 5.0, 2.0, 12.0},
				{3.0, 4.0, 1.0, -1.0, 4.0},
				{-1.0, -3.0, 1.0, 3.0, -1.0}};

		SimultaneousEquation t1 = new SimultaneousEquation(test1);
		t1.solveByGaussJordan();
	}
}

/* 実行結果
 [ 2.00 1.00 3.00 4.00 2.00 ]
 [ 3.00 2.00 5.00 2.00 12.00 ]
 [ 3.00 4.00 1.00 -1.00 4.00 ]
 [ -1.00 -3.00 1.00 3.00 -1.00 ]

 1行1列目が１となるように割り，他の行の1列目が０となるように引く
 [ 1.00 0.50 1.50 2.00 1.00 ]
 [ 0.00 0.50 0.50 -4.00 9.00 ]
 [ 0.00 2.50 -3.50 -7.00 1.00 ]
 [ 0.00 -2.50 2.50 5.00 0.00 ]

 2行2列目が１となるように割り，他の行の2列目が０となるように引く
 [ 1.00 0.00 1.00 6.00 -8.00 ]
 [ 0.00 1.00 1.00 -8.00 18.00 ]
 [ 0.00 0.00 -6.00 13.00 -44.00 ]
 [ 0.00 0.00 5.00 -15.00 45.00 ]

 3行3列目が１となるように割り，他の行の3列目が０となるように引く
 [ 1.00 0.00 0.00 8.17 -15.33 ]
 [ 0.00 1.00 0.00 -5.83 10.67 ]
 [ -0.00 -0.00 1.00 -2.17 7.33 ]
 [ 0.00 0.00 0.00 -4.17 8.33 ]

 4行4列目が１となるように割り，他の行の4列目が０となるように引く
 [ 1.00 0.00 0.00 0.00 1.00 ]
 [ 0.00 1.00 0.00 0.00 -1.00 ]
 [ -0.00 -0.00 1.00 0.00 3.00 ]
 [ -0.00 -0.00 -0.00 1.00 -2.00 ]

Answer:
x1 = 1.00, x2 = -1.00, x3 = 3.00, x4 = -2.00.
 */
