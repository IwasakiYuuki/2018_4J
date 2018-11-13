//4J04Šâè—I‹I@‰Û‘è”Ô†‚U
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
			System.out.println((i+1)+"s"+(i+1)+"—ñ–Ú‚ª‚P‚Æ‚È‚é‚æ‚¤‚ÉŠ„‚èC‘¼‚Ìs‚Ì"+(i+1)+"—ñ–Ú‚ª‚O‚Æ‚È‚é‚æ‚¤‚Éˆø‚­");
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
				System.out.print("._n");
			}
		}
	}

	public void subtractRowFromPivot(int pivot, int n, int row){
	    int num = this.m[row].length, i;
	    double rate = this.m[row][n]/this.m[pivot][n];
	    for(i=0; i<num; i++){
	        this.m[row][i] -= (this.m[pivot][i]*rate);
	    }
	}

	public void solveByGauss(){
	    int i, j, cnt=1, num, current;
	    double buf;
	    num = this.m.length;
        for(i=0; i<num; i++){
            this.display();
            System.out.println("");
            for(j=cnt; j<num; j++){
                this.subtractRowFromPivot(i, i, j);
            }
            cnt++;
        }
        cnt=0;
        for(i=0; i<num; i++){
            buf = 0;
            for(j=0; j<cnt; j++){
                current = num-j-1;
                buf += this.m[num-i-1][current]*this.answers[current];
            }
            current = num-i-1;
            this.answers[current] = (this.m[current][this.m[0].length-1] - buf)/this.m[current][current];
            cnt++;
        }
		System.out.println("Answer:");
		for(i=0; i<this.m.length; i++){
			System.out.printf("x%d = %.2f", i+1, this.answers[i]);
			if(i!=(this.m.length-1)){
				System.out.print(", ");
			}else{
				System.out.print("._n");
			}
		}
	}

	public static void main(String[] args) {
		double[][] test1 = {{2.0, 1.0, 3.0, 4.0, 2.0},
				{3.0, 2.0, 5.0, 2.0, 12.0},
				{3.0, 4.0, 1.0, -1.0, 4.0},
				{-1.0, -3.0, 1.0, 3.0, -1.0}};

		SimultaneousEquation t1 = new SimultaneousEquation(test1);
		t1.solveByGauss();
	}
}

/* ÀsŒ‹‰Ê
 [ 2.00 1.00 3.00 4.00 2.00 ]
 [ 3.00 2.00 5.00 2.00 12.00 ]
 [ 3.00 4.00 1.00 -1.00 4.00 ]
 [ -1.00 -3.00 1.00 3.00 -1.00 ]

 [ 2.00 1.00 3.00 4.00 2.00 ]
 [ 0.00 0.50 0.50 -4.00 9.00 ]
 [ 0.00 2.50 -3.50 -7.00 1.00 ]
 [ 0.00 -2.50 2.50 5.00 0.00 ]

 [ 2.00 1.00 3.00 4.00 2.00 ]
 [ 0.00 0.50 0.50 -4.00 9.00 ]
 [ 0.00 0.00 -6.00 13.00 -44.00 ]
 [ 0.00 0.00 5.00 -15.00 45.00 ]

 [ 2.00 1.00 3.00 4.00 2.00 ]
 [ 0.00 0.50 0.50 -4.00 9.00 ]
 [ 0.00 0.00 -6.00 13.00 -44.00 ]
 [ 0.00 0.00 0.00 -4.17 8.33 ]

 Answer:
 x1 = 1.00, x2 = -1.00, x3 = 3.00, x4 = -2.00.
 */
