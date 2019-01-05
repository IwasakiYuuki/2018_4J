//4J04���I�I�@�ۑ�ԍ��X�|�P
package pr2calc;

public class NonlinearEquation{

    public static final double EPSILON = 0.001;
    public static final int MAXIMUM_IT = 100;
    public static final double NEGATIVE_MAX = 0.0;
    public static final double POSITIVE_MAX = 4.0;

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

    private void _solveNLEByBisectionMethod(){
        double value, pastValue, left, right, y;
        int i;

        value = 0.0;
        pastValue = 0.0;
        left = NEGATIVE_MAX;
        right = POSITIVE_MAX;

        for(i=0; i<MAXIMUM_IT; i++){
            value = (right - left)/2.0 + left;
            if((value + 2.0) == 0){
                y = 1.0;
            }else{
                y = Math.sin(value+2.0)/(value+2.0);
            }
            if(y > 0){
                left = value;
            }else if(y < 0){
                right = value;
            }
            System.out.printf("xMid = %f, f(xMid)=%f, xPastMid = %f\n", value, y, pastValue);
            if(Math.abs(y) < EPSILON){
                break;
            }
            pastValue = value;
        }
        this.answer_ = value;
        this.iteration_ = i;
    }


    public static void main(String[] args) {
        NonlinearEquation eqn = new NonlinearEquation(2.0);

            // ������������ݒ�
            eqn._solveNLEByBisectionMethod();
            if(eqn.iteration_ < MAXIMUM_IT){
                System.out.println("x = "+eqn.answer_+" at iteration "+eqn.iteration_);
            }else{
                System.out.println("����������܂���ł���");
            }
            // �ߎ���������ꂽ��ߎ���������ڂ̌J��Ԃ��ŉ�������ꂽ����\��
            // �����Ȃ���΁A����������Ȃ��������Ƃ�\��
    }
}

/*----------���s����----------
 xMid = 2.000000, f(xMid)=-0.189201, xPastMid = 0.000000
 xMid = 1.000000, f(xMid)=0.047040, xPastMid = 2.000000
 xMid = 1.500000, f(xMid)=-0.100224, xPastMid = 1.000000
 xMid = 1.250000, f(xMid)=-0.033291, xPastMid = 1.500000
 xMid = 1.125000, f(xMid)=0.005309, xPastMid = 1.250000
 xMid = 1.187500, f(xMid)=-0.014397, xPastMid = 1.125000
 xMid = 1.156250, f(xMid)=-0.004644, xPastMid = 1.187500
 xMid = 1.140625, f(xMid)=0.000308, xPastMid = 1.156250
 x = 1.140625 at iteration 7
----------------------------*/
