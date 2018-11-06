package pr2calc;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SourceExample{  

	public int a;            // ����3�ϐ��E�E�E�t�B�[���h�ϐ��i���\�b�h�̊O�ɂ���j�B
	public int[][] b;        // �N���X���́Amain()���\�b�h�������S���\�b�h�ŗ��p�\
	public String str;       

	/* �ȉ��̃��\�b�h�i�֐��j�̓R���X�g���N�^�B�I�u�W�F�N�g�̐����i& �������j���s�� 
	 * �����̐ݒ�͎��R�B�������قȂ�΁A�����p�ӂ��Ă��ǂ�
	 */
	public SourceExample(){   // �e�ϐ��̏����������s�i��Ȃ̂ŁA���g�͓K���ł��j
		int h,i;    // ���\�b�h���Ő錾�����ϐ��́A���\�b�h���݂̂ŗ��p��

		this.a = -1;    // �t�B�[���h�ϐ��Ƃ��Ē�`���ꂽ�ϐ��́A�N���X���ǂ��ł����p�ithis.a �E�E�E "����(= this)�I�u�W�F�N�g(�C���X�^���X)���g�̎��ϐ�a"�j
		this.b = new int[2][2];

		/* java �ł́A�z��ƕ�����̓I�u�W�F�N�g�Ƃ��ėp�ӂ���Ă��邽�߁A�V�����z��A
		 * �������錾���鎞�ɂ� "new" ���g��(��̗�ł́Ab ��2�~2�̐����^�񎟌��z��
		 * �Ƃ��Đ錾���Ă���j
		 */

		for(h=0;h<this.b.length;h++){		// �񎟌��z��̒l�� -1 �ŏ�����
			for(i=0;i<this.b[0].length;i++)		// �z��Ɋւ��ẮA"new" ��p������
				this.b[h][i] = -1;			// ���������邱�Ƃ��\�i�ڍׂ�main
		}						// ���\�b�h�����Q�Ɓj

		// this.b.length : �z��̑��v�f�̗v�f���Athis.b[0].length : �z��̑��v�f�̗v�f��

		this.str = "";	// ������^�̓I�u�W�F�N�g�Ƃ��ėp�ӂ����ƋL�q�������A���ɂ悭
	}			// �p������^�ł��邽�߁A���̂悤�ȋL�@���F�߂��Ă���
	// �i new String(""); �Ƃ����������ł����ROK �j


	// �t�B�[���h�ϐ�a, b, str�̒l������ŏ������i�ݒ�j����R���X�g���N�^��������L�q����



	/* �ȍ~�Amain���\�b�h�ȊO�̃��\�b�h�́A�R���X�g���N�^�Ő������ꂽ�I�u�W�F�N�g
	 * ����Ď��s�����
	 */

	public void setA(int value){    // ���\�b�h��ʂ��āA�t�B�[���h�ϐ��̒l��ݒ肷��
		this.a = value;		// �isetter�ƌĂ΂��j
	}

	public int getA(){		// ���\�b�h��ʂ��āA�t�B�[���h�ϐ��̒l��Ԃ�
		return this.a;		// �igetter�ƌĂ΂��j
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
		// �e���ŕK�v�ȓ��e���L�q���邱��
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
			// �t�@�C��"fileName"����A�f�[�^��ǂݍ��ރ��\�b�h���Ăяo��
			loadData(fileName);
		}catch(IOException e){  System.out.println("�t�@�C������̓��͂Ɏ��s���܂����B");}
	}

	private boolean loadData(String fileName) throws IOException{
		int h,i;
		int row, column;
		BufferedReader fin = new BufferedReader(new FileReader(fileName));
		String inputData;
		String[] inputValue;

		// inputData �ɁA�t�@�C�����當�������s���ǂݍ���
		// ����ꂽ������f�[�^���A�X�y�[�X(= "�_�_s") �ŋ�؂�A�z�� inputValue �֊i�[
		inputData = fin.readLine();
		inputValue = inputData.split("�_�_s", 0);

		if(inputValue.length != 1)
			return false;
		else{
			// �t�B�[���h�i�C���X�^���X�j�ϐ� a �ɁAinputValue�̍ŏ��i0�Ԗځj�̗v�f����
			this.setA(Integer.parseInt(inputValue[0]));
			// ������s�ǂݍ��݁A�X�y�[�X��؂�� inputValue �փf�[�^(���ɓǂݍ��ލs��̍s�����񐔁j���i�[
			inputData = fin.readLine();
			inputValue = inputData.split("�_�_s", 0);
			if(inputValue.length != 2)	// �s�����񐔂̑o�����i�[����Ă��Ȃ����
				return false;
			else{
				// �ϐ� row �ɍs���̃f�[�^�Acolumn �ɗ񐔂̃f�[�^����
				row = Integer.parseInt(inputValue[0]);
				column = Integer.parseInt(inputValue[1]);
				this.b = new int[row][column];
				// �z�� b ��h�si��ڂ̗v�f�ɁA�ǂ݂񂾃t�@�C����h�s�ځA(������ji�Ԗڂ̃f�[�^���i�[
				for(i=0; i<row; i++){
					inputData = fin.readLine();
					inputValue = inputData.split("�_�_s", 0);
					for(h=0; h<column; h++){
						this.b[i][h] = Integer.parseInt(inputValue[h]);
					}
				}
				// �Ō�Ɉ�s�ǂݍ��݁A�X�y�[�X��؂�� inputValue �փf�[�^(������j���i�[
				inputData = fin.readLine();
				// �t�B�[���h�i�C���X�^���X�j�ϐ� str �ɁA�i�[�������������
				this.setStr(inputData); 
			}
		}
		fin.close(); 
		return true;
	}	

	public static void main(String[] args) {
		SourceExample    ex;    // SourceExample �N���X�̃I�u�W�F�N�g ex ��錾

		// ���̓f�[�^�̊܂܂�Ă���t�@�C������sample.dat�Ƃ����ꍇ
		ex = new SourceExample(args[0]);

		System.out.println("�I�u�W�F�N�g��a�t�B�[���h�̒l��"+ex.getA()+"�ł�");
		System.out.println("");
		ex.showAllContentsOfB();
		System.out.println("");
		System.out.println(ex.getStr());

	}
}
