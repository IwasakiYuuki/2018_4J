//4J04���I�I
package pr2calc;

public class Matrix {
	// �K�v�ȃt�B�[���h�i�C���X�^���X�j�ϐ���錾����
	public double[][] m;
	public int numOfRow;
	public int numOfColumn;

	public Matrix(){
		// �s��̍s��,�񐔂��i�[����C���X�^���X�ϐ��̒l��0�ɏ�����
		this.numOfRow = 0;
		this.numOfColumn = 0;
	}

	public Matrix(double[][] input){
		// �񎟌��z�� input �̓��e�ŁA�s��i�C���X�^���X�ϐ��j������������(��F�z�� input��0�s0��ڂ̒l���A�s���0�s0��ڂƂ���)
		m = new double[input.length][input[0].length];
		int i, j;
		for(i=0; i<input.length; i++){
			for(j=0; j<input[0].length; j++){
				m[i][j] = input[i][j];
			}
		}
	}

	public Matrix(double[] input){
		// �ꎟ���z�� input �̓��e�ŁA�s��i�C���X�^���X�ϐ��j������������(��F�s����1�A�񐔂�input�̗v�f���Ƃ���)
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
		// �w�肵���͈͂����݂��Ȃ��ꍇ
		if(this.m.length<rowIndex && this.m[0].length<columnIndex && rowIndex >0 && columnIndex > 0){
			System.out.println("�w�肷��v�f�͑��݂��܂���.");
			System.exit(0);
		}
		// �w�肳�ꂽ�v�f�ɑΉ�����l��Ԃ�
		return this.m[rowIndex][columnIndex];
	}

	public void display(){
		// �s����e�̕\����������������
		int i, j;
		for(i=0; i<m.length; i++){
			System.out.print("[ ");
			for(j=0; j<m[0].length; j++){
				System.out.printf("%.2f ", m[i][j]);
			}
			System.out.print("]�_n");
		}
	}

	// �x�N�g��A��B�̓��� A�EB�̌��ʂ�Ԃ�
	public double getInnerProduct(Matrix b){
		double sum = 0;
		int i;
		// A����x�N�g���ł���ꍇ�A�G���[���b�Z�[�W��\�������� System.exit(0)
		if(this.m[0].length == 1){
			System.out.println("[ERROR] : Vector's length is not enough.");
			System.exit(0);
		}
		// A, B �o���Ƃ��s�x�N�g���A���A�v�f������������Γ��ς��v�Z
		if(this.m.length == 1 && b.m.length == 1 && this.m[0].length == b.m[0].length){
			for(i=0; i<b.m[0].length; i++){
				sum += this.m[0][i]*b.m[0][i];
			}
		}
		// A���s�x�N�g���AB����x�N�g���ŁA�v�f������������Γ��ς��v�Z
		else if(this.m.length == 1 && b.m[0].length == 1 && this.m[0].length == b.m.length){
			for(i=0; i<b.m[0].length; i++){
				sum += this.m[0][i]*b.m[i][0];
			}
		}
		// ���όv�Z���\�ȏ����𖞂����Ȃ��ꍇ�́A�G���[���b�Z�[�W��\��������System.out.exit(0)
		else {
			System.out.println("[ERROR] : It does not meet the condition.");
			System.exit(0);
		}
		// �v�Z���ʂ�Ԃ�
		return sum;
	}

	// �s�񓯎m�A�������͍s��ƃx�N�g���Ƃ̐ς��v�Z����
	public Matrix multiplyMatrix(Matrix target){
		Matrix buf = new Matrix(new double[this.m.length][target.m[0].length]);
		double sum;
		int i ,j ,k;
		// �|������s��̗񐔂Ɗ|����s��̍s�����������Ȃ�
		if(this.multipliable(target)){
			// �ς̌v�Z��������������
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
			System.out.println("�v�f�����v�Z�ł���g�ݍ��킹�ƂȂ��Ă��܂���");
			System.exit(0);
		}
		// �ς̌��ʂ�Matrix�^�ŕԂ�
		return buf;
	}

	public boolean multipliable(Matrix y) {
		if(this.m[0].length == y.m.length ){
			return true;
		}else{
			System.out.println("�v�f�����v�Z�ł���g�ݍ��킹�ƂȂ��Ă��܂���");
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
		 * main ���\�b�h���ō���쐬�������όv�Z���\�b�h��s�񓯎m�A�x�N�g���ƍs��A
		 * �s��ƃx�N�g���̐ς��v�Z���郁�\�b�h������ɓ����Ă��邩���m�F����B
		 */

		// �s��E�x�N�g����`�A����щ��Z�����̈�� �i�����܂ň��ł��j�@�ۑ�̗v���𖞂����悤�A�e���ŉ��M�E�C�����Ă�������
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
//		// �ȉ��́A�s��E�x�N�g�����Z�̎��s�����ʕ\���̈��D�s�v�ł���΍폜���C�ۑ�̏����𖞂����L�q��V���ɒǉ����邱��
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
//		System.out.println("�_n(1)");
//		System.out.println("m1_0 �� m1_1 ���� ");
//		System.out.println(mat1_0.getInnerProduct(mat1_1));
//
//		System.out.println("�_n(2)");
//		System.out.println("m2_0 * m2_1 = ");
//		if(mat2_0.multipliable(mat2_1) == true)
//			(mat2_0.multiplyMatrix(mat2_1)).display();
//
//		System.out.println("�_n(3)");
//		System.out.println("m3_0 * m3_1 = ");
//		if(mat3_0.multipliable(mat3_1) == true)
//			(mat3_0.multiplyMatrix(mat3_1)).display();
//
//		System.out.println("�_n(4)");
//		System.out.println("m4_0 * m4_1 = ");
//		if(mat4_0.multipliable(mat4_1) == true)
//			(mat4_0.multiplyMatrix(mat4_1)).display();
//
//		System.out.println("�_n(5)");
//		System.out.println("m5_0 * m5_1 = ");
//		if(mat5_0.multipliable(mat5_1) == true)
//			(mat5_0.multiplyMatrix(mat5_1)).display();
//		System.out.println("");
//		System.out.println(mat1_0.convertIntoRadian(30));
	}
}
