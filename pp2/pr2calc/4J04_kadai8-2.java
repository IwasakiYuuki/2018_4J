//4J04���I�I�@�ۑ�ԍ��W
package pr2calc;

public class NonlinearEquation{

    public static final double EPSILON = 0.001;
    public static final int MAXIMUM_IT = 100;

    private double initialValue_;
    private double answer_;
    private int iteration_;

    // �R���X�g���N�^(�Œ��p�ӂ���j
    public NonlinearEquation(double initialValue_){
            this.initialValue_ = initialValue_;
    }

    private void _solveNLEByLinearIteration(){
        double value,      // x_k �ɑΉ�
               pastValue;  // x_{k-1} �ɑΉ��i�����pastValue = x_0�Ƃ���j
        int i;

        // ������������value�ɐݒ肵
        value = this.initialValue_;
        for(i=0; i<MAXIMUM_IT; i++){
            pastValue = value;
            value = Math.sqrt(14+pastValue);
            System.out.println("value = "+value+", pastValue = "+pastValue);
            if(Math.abs(value-pastValue)<EPSILON){
                break;
            }
        }
        this.answer_ = value;
        this.iteration_ = i;
        // |value - pastValue| �� EPSILON �����ƂȂ�(�ߎ�����������)�A��������
        // �J��Ԃ��񐔂�MAXIMUM_IT ��ɓ��B����܂ŌJ��Ԃ�
        // �J��Ԃ��œ����锽�����̓r���o�߂�\������悤�ɂ��邱��
    }


    public static void main(String[] args) {
        NonlinearEquation eqn = new NonlinearEquation(2.0);

            // ������������ݒ�
            eqn._solveNLEByLinearIteration();
            if(eqn.iteration_ < MAXIMUM_IT){
                System.out.println("x = "+eqn.answer_+" at iteration "+eqn.iteration_);
            }else{
                System.out.println("����������܂���ł���");
            }
            // �ߎ���������ꂽ��ߎ���������ڂ̌J��Ԃ��ŉ�������ꂽ����\��
            // �����Ȃ���΁A����������Ȃ��������Ƃ�\��
    }
}